package ke.derrick.steps.util

import java.time.LocalTime
import java.util.*

fun getDayOfTheWeek(): Int {
    val c = Calendar.getInstance()
    c.time = Date()
    return c.get(Calendar.DAY_OF_WEEK) // starting from 1
}

fun getCurrentHourMinute() =
    Pair(LocalTime.now().hour, LocalTime.now().minute)

fun convertToTwoDigitNumberString(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

