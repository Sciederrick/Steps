package ke.derrick.steps.domain

import ke.derrick.steps.DaysOfTheWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class CreateScheduleUseCase {
    operator fun invoke(scheduledDayOrdinal: Int, mHour: Int, mMinute: Int) {
        val today = LocalDate.now().toString()
        val mDayOfWeekStr = LocalDate.parse(today).dayOfWeek.toString().uppercase(Locale.ROOT)
        val todaysOrdinal = DaysOfTheWeek.valueOf(mDayOfWeekStr).ordinal
        val numDays = scheduledDayOrdinal - todaysOrdinal
        val scheduledDate = LocalDate.now().plusDays(numDays.toLong())
        val localTime = LocalTime.of(mHour, mMinute)
        // TODO: use this: "${scheduledDate}T${localTime}" to schedule the task with alarm manager
    }
}