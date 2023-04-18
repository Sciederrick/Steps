package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.SCHEDULE_NOTIF_ID
import ke.derrick.steps.domain.SnoozeWorkoutReminderUseCase
import ke.derrick.steps.util.NotifUtils

class SnoozeWorkoutNotificationReceiver(): BroadcastReceiver() {
    private val snoozeWorkoutReminderUseCase = SnoozeWorkoutReminderUseCase()
    override fun onReceive(mContext: Context?, mIntent: Intent?) {
        if (mContext != null && mIntent != null) {
            val notification = NotifUtils(mContext)
            snoozeWorkoutReminderUseCase(mContext,
                mIntent.getStringExtra(EXTRA_SCHEDULE_NOTIF_TIME) ?: "Snoozed",
                30 * 60 * 1000)
            notification.cancelNotification(SCHEDULE_NOTIF_ID)
        }
    }
}