package ke.derrick.steps

import androidx.compose.ui.graphics.Color
import ke.derrick.steps.ui.theme.Blue800
import ke.derrick.steps.ui.theme.Green400
import ke.derrick.steps.ui.theme.Pink700
import ke.derrick.steps.ui.theme.White

enum class DaysOfTheWeek(val shortName: String) {
    SUNDAY("Sun"), MONDAY("Mon"), TUESDAY("Tue"),
    WEDNESDAY("Wed"), THURSDAY("Thu"), FRIDAY("Fri"),
    SATURDAY("Sat")
}

enum class WorkoutStatus(val color: Color) {
    DONE(Green400), MISSED(Pink700), PENDING(Blue800), SCHEDULED(White)
}

const val CHANNEL_ID = ""