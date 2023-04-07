package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.utils.NotifUtils

class CreateWorkoutNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(mContext: Context?, mIntent: Intent?) {
        if (mContext != null && mIntent != null) {
            val notification = NotifUtils(mContext)
            notification.makeStepsReminderNotification("Reminder", "It's workout time!",
                mIntent.getStringExtra(EXTRA_SCHEDULE_NOTIF_TIME) ?: "")
        }
    }
}