package ke.derrick.steps.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import ke.derrick.steps.ONGOING_NOTIF_ID
import ke.derrick.steps.utils.NotifUtils
import ke.derrick.steps.utils.makeStepsOngoingNotification

class StepsTrackerService: Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var notif: NotifUtils
    private var isSensorPresent = true

    override fun onCreate() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).also {
                sensor ->
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

        val notification = notif.makeStepsOngoingNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(ONGOING_NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
        } else {
            startForeground(ONGOING_NOTIF_ID, notification)
        }

        return START_STICKY // The service can be paused and resumed
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            notif.updateStepsOngoingNotification("${event.values[0].toInt()}")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    companion object {
        const val TAG = "StepsTrackerService"
        fun startService(context: Context) {
            val startIntent = Intent(context, StepsTrackerService::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, StepsTrackerService::class.java)
            context.stopService(stopIntent)
        }
    }
}