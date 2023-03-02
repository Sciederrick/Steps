package ke.derrick.steps.ui.utils

import java.util.*

fun getDayOfTheWeek(): Int {
    val c = Calendar.getInstance()
    c.time = Date()
    return c.get(Calendar.DAY_OF_WEEK) // starting from 1
}

fun getHourMinute(): Pair<Int, Int> {
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    return Pair(mHour, mMinute)
}