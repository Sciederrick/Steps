package ke.derrick.steps.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import ke.derrick.steps.ONGOING_NOTIF_ID
import ke.derrick.steps.IsServiceStart
import ke.derrick.steps.StepsApplication
import ke.derrick.steps.data.local.entities.Steps
import ke.derrick.steps.data.repository.Repository
import ke.derrick.steps.utils.NotifUtils
import ke.derrick.steps.utils.convertToTwoDigitNumberString
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import kotlin.properties.Delegates

class StepsTrackerService: Service(), SensorEventListener, CoroutineScope {
    private lateinit var sensorManager: SensorManager
    private lateinit var notif: NotifUtils
    private var isSensorPresent = true
    private val binder = LocalBinder() // Binder given to clients.

    private var _numSteps = MutableStateFlow(0L)
    var numSteps = _numSteps.asStateFlow()

    private var cumulativeStepCount = 0L

    private lateinit var currentDate: LocalDate
    private var startingStepCount by Delegates.notNull<Long>()
    private lateinit var repository: Repository

    private var job = Job()
    override val coroutineContext
        get() = job + Dispatchers.IO
    private lateinit var initStartingStepCount: Job

    private var existingRecord: Steps? = null
    private var isUpdate = false

    override fun onCreate() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).also { sensor ->
            if (sensor != null) {
                sensorManager
                    .registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                isSensorPresent = false
            }

        }

        notif = NotifUtils(applicationContext)

        repository = (applicationContext as StepsApplication).repo

        currentDate = LocalDate.now()

        initStartingStepCount = launch {
            val lastRecord = async { repository.getLastStepCount() }.await()
            startingStepCount = repository.getInitialStepCount() ?: 0L
            // check if the date (YYYY-MM-DD) match.
            // if they do match, the next write to the DB will be an update
            isUpdate =
                (lastRecord?.createdAt?.split("T")?.get(0) ?: "") == currentDate.toString()
            if (isUpdate) existingRecord = lastRecord

            Log.d(TAG, "starting step count: $startingStepCount")
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        currentDate = LocalDate.now()
        when(intent?.getIntExtra(IsServiceStart.SERVICE_STOP.title, 1)) {
            0 -> {
                stopSelf(startId)
            }
            1 -> {
                launch(Dispatchers.Main) {
                    initStartingStepCount.join()
                    val notification = notif.makeStepsOngoingNotification()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(ONGOING_NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
                    } else {
                        startForeground(ONGOING_NOTIF_ID, notification)
                    }
                }

            }

        }

        return START_STICKY // The service can be paused and resumed
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            cumulativeStepCount = event.values[0].toLong()
            launch(Dispatchers.Main) {
                initStartingStepCount.join()
                _numSteps.value = cumulativeStepCount - startingStepCount
                Log.d(TAG, "${_numSteps.value}")
                notif.updateStepsOngoingNotification("${_numSteps.value}")
            }

            // It's a new day hence store the last day's value
            // otherwise, store step count onDestroy since we'll make less calls to the DB this way
            if (currentDate != LocalDate.now()) {
                syncStepCountWithDB()
                currentDate = LocalDate.now()
            }
        }
    }

    private fun syncStepCountWithDB() {
        launch {
            if (isUpdate) {
                val workout = repository.createSteps(
                    id = existingRecord!!.id,
                    count = existingRecord!!.count + _numSteps.value,
                    day = convertToTwoDigitNumberString(
                        LocalDate.parse(currentDate.toString()).dayOfMonth
                    )
                )
                repository.updateStepCount(workout)
            } else {
                val workout = repository.createSteps(
                    count = _numSteps.value,
                    day = convertToTwoDigitNumberString(
                        LocalDate.parse(currentDate.toString()).dayOfMonth
                    )
                )
                repository.insertStepCount(workout)
            }
        }
    }

    private fun syncInitialStepCount() {
        launch {
            if (cumulativeStepCount > 0) repository.persistInitialStepCount(cumulativeStepCount)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        syncStepCountWithDB()
        syncInitialStepCount()
        sensorManager.unregisterListener(this)
        job.cancel()
    }

    inner class LocalBinder : Binder() {
        // Return this instance of StepsTrackerService so clients can call public methods.
        fun getService(): StepsTrackerService = this@StepsTrackerService
    }

    companion object {
        const val TAG = "StepsTrackerService"
        fun startService(context: Context) {
            val startIntent = Intent(context, StepsTrackerService::class.java).apply {
                putExtra(IsServiceStart.SERVICE_START.title, IsServiceStart.SERVICE_START.ordinal)
            }
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            Log.d(TAG, "trying to stop a service")
            val stopIntent = Intent(context, StepsTrackerService::class.java)
            context.stopService(stopIntent)
        }
    }
}