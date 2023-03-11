package ke.derrick.steps.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import ke.derrick.steps.BROADCAST_REQUEST_CODE
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.EXTRA_SCHEDULE_NOTIF_TIME
import ke.derrick.steps.receiver.CreateWorkoutNotificationReceiver

class SnoozeWorkoutReminderUseCase {
    operator fun invoke(mContext: Context, scheduledTime: String, durationMillis: Long) {
        // Allow system alarm to call our receiver
        val scheduleIntent = Intent(mContext, CreateWorkoutNotificationReceiver::class.java).apply {
            putExtra(EXTRA_SCHEDULE_NOTIF_TIME, scheduledTime)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            mContext.applicationContext, BROADCAST_REQUEST_CODE, scheduleIntent, 0)
        // Call Alarm Manager
        val alarmManager = mContext.applicationContext.getSystemService(Context.ALARM_SERVICE)
        AlarmManagerCompat.setExact(
            alarmManager as AlarmManager, AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + durationMillis, pendingIntent)
    }
}