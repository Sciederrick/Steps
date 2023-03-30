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

enum class IsServiceStart(val title: String) {
    SERVICE_STOP("IsServiceStart"), SERVICE_START("IsServiceStart")
}

const val SCHEDULE_NOTIF_CHANNEL_ID = "STEPS_01"
const val SCHEDULE_NOTIF_ID = 1
const val ONGOING_NOTIF_CHANNEL_ID = "STEPS_02"
const val ONGOING_NOTIF_ID = 2
const val EXTRA_SCHEDULE_NOTIF_TIME = "ke.derrick.steps.SCHEDULED_TIME"
const val BROADCAST_REQUEST_CODE = 12345
