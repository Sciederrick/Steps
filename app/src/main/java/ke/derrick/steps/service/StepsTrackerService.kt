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
import ke.derrick.steps.utils.NotifUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StepsTrackerService: Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var notif: NotifUtils
    private var isSensorPresent = true
    private val binder = LocalBinder() // Binder given to clients.
    private var _numSteps = MutableStateFlow(0)

    var numSteps = _numSteps.asStateFlow()

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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when(intent?.getIntExtra(IsServiceStart.SERVICE_STOP.title, 1)) {
            0 -> {
                Log.d(TAG, "stop service from stop self")
                stopSelf(startId)
            }
            1 -> {
                val notification = notif.makeStepsOngoingNotification()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(ONGOING_NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
                } else {
                    startForeground(ONGOING_NOTIF_ID, notification)
                }
            }

        }

        return START_STICKY // The service can be paused and resumed
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            _numSteps.value = event.values[0].toInt()
            notif.updateStepsOngoingNotification("${_numSteps.value}")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        Log.d(TAG, "service destroyed")
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