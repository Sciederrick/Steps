package ke.derrick.steps.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import ke.derrick.steps.ONGOING_NOTIF_ID
import ke.derrick.steps.utils.makeStepsOngoingNotification

class StepsTrackerService: Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val notification = makeStepsOngoingNotification(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(ONGOING_NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
        } else {
            startForeground(ONGOING_NOTIF_ID, notification)
        }
        startForeground(ONGOING_NOTIF_ID, makeStepsOngoingNotification(applicationContext))

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}