package ke.derrick.steps.ui.utils

import java.util.*

fun getDayOfTheWeek(): Int {
    val c = Calendar.getInstance()
    c.time = Date()
    return c.get(Calendar.DAY_OF_WEEK)
}