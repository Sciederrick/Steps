package ke.derrick.steps

import androidx.compose.ui.graphics.Color
import ke.derrick.steps.ui.theme.Blue800
import ke.derrick.steps.ui.theme.Green400
import ke.derrick.steps.ui.theme.Pink700
import ke.derrick.steps.ui.theme.Purple40

enum class DaysOfTheWeek(val shortName: String) {
    SUNDAY("Sun"), MONDAY("Mon"), TUESDAY("Tue"),
    WEDNESDAY("Wed"), THURSDAY("Thu"), FRIDAY("Fri"),
    SATURDAY("Sat")
}

enum class ScheduleStatus(val color: Color) {
    DONE(Green400), MISSED(Pink700), SCHEDULED(Blue800)
}