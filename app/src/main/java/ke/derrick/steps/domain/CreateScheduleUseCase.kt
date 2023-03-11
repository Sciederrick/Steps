package ke.derrick.steps.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.receiver.ScheduleReceiver
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*

class CreateScheduleUseCase {
    operator fun invoke(mContext: Context, scheduledDayOrdinal: Int, mHour: Int, mMinute: Int) {
        // Convert the scheduled day ordinal to a corresponding date in YYYY-MM-DD
        val today = LocalDate.now().toString()
        val mDayOfWeekStr = LocalDate.parse(today).dayOfWeek.toString().uppercase(Locale.ROOT)
        val todaysOrdinal = DaysOfTheWeek.valueOf(mDayOfWeekStr).ordinal
        val numDays = scheduledDayOrdinal - todaysOrdinal
        val scheduledDate = LocalDate.now().plusDays(numDays.toLong())
        val localTime = LocalTime.of(mHour, mMinute)


        // Convert the scheduled date time to milliseconds
        val scheduledDateTime = LocalDateTime.parse("${scheduledDate}T${localTime}")
        val durationMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), scheduledDateTime)

        // Allow system alarm to call our receiver
        val scheduleIntent = Intent(mContext, ScheduleReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            mContext.applicationContext,12345, scheduleIntent,0)

        val alarmManager = mContext.applicationContext.getSystemService(ALARM_SERVICE)
        AlarmManagerCompat.setExact(
            alarmManager as AlarmManager, AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + durationMillis
            , pendingIntent)

    }
}