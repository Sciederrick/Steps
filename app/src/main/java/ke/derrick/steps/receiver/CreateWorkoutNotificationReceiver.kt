package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.utils.makeStepsReminderNotification

class CreateWorkoutNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null && p1 != null) {
            makeStepsReminderNotification(p0, "Reminder", "It's workout time!",
                p1.getStringExtra(EXTRA_SCHEDULE_NOTIF_TIME) ?: "")
        }
    }
}