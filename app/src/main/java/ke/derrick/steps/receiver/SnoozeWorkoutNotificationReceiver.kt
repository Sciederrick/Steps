package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.SCHEDULE_NOTIF_ID
import ke.derrick.steps.domain.SnoozeWorkoutReminderUseCase
import ke.derrick.steps.utils.cancelNotification

class SnoozeWorkoutNotificationReceiver(): BroadcastReceiver() {
    private val snoozeWorkoutReminderUseCase = SnoozeWorkoutReminderUseCase()
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null && p1 != null) {
            snoozeWorkoutReminderUseCase(p0,
                p1.getStringExtra(EXTRA_SCHEDULE_NOTIF_TIME) ?: "",
                1 * 60 * 1000)
            cancelNotification(p0, SCHEDULE_NOTIF_ID)
        }
    }
}