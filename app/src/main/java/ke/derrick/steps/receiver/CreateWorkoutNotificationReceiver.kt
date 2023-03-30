package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.utils.NotifUtils

class CreateWorkoutNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(mContext: Context?, p1: Intent?) {
        if (mContext != null && p1 != null) {
            val notification = NotifUtils(mContext)
            notification.makeStepsReminderNotification("Reminder", "It's workout time!",
                p1.getStringExtra(EXTRA_SCHEDULE_NOTIF_TIME) ?: "")
        }
    }
}