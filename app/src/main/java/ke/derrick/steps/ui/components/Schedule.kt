package ke.derrick.steps.ui.components

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.R
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.ui.theme.Gray900
import ke.derrick.steps.ui.theme.RoundedShapes
import ke.derrick.steps.ui.theme.White
import ke.derrick.steps.utils.getDayOfTheWeek
import kotlin.properties.Delegates

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Schedule(dayWithWorkoutStatus: Array<Int>, onSchedule: (Int) -> Unit = {}) {
    Column(modifier = Modifier.padding(
        vertical = dimensionResource(R.dimen.section_spacing_vertical)
    )) {

        Text(text = stringResource(id = R.string.section_schedule_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp))
        Row(modifier = Modifier
            .widthIn()
            .horizontalScroll(rememberScrollState())) {
            val today = getDayOfTheWeek() - 1
            var status  = WorkoutStatus.MISSED.ordinal

            for(dayOfTheWeek in DaysOfTheWeek.values()) {
                if (dayWithWorkoutStatus.isNotEmpty() && dayOfTheWeek.ordinal < dayWithWorkoutStatus.size)
                    status = dayWithWorkoutStatus[dayOfTheWeek.ordinal]

                val (textColor, bgColor, isScheduleCardEnabled)
                    = initializeComponentColors(dayOfTheWeek, today, status)

                Card(
                    onClick = { onSchedule(dayOfTheWeek.ordinal) },
                    enabled = isScheduleCardEnabled,
                    backgroundColor = bgColor,
                    modifier = Modifier.padding(6.dp).testTag("ScheduleCard"),
                    shape = RoundedShapes.small) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(54.dp)
                            .padding(vertical = 16.dp)) {
                        Text(text = dayOfTheWeek.shortName, color = textColor)
                        Spacer(modifier = Modifier.size(14.dp))

                        DisplayIcon(dayOfTheWeek, today, status)

                    }

                }
            }

        }
    }

}

@Composable
fun DisplayIcon(dayOfTheWeek: DaysOfTheWeek, today: Int, status: Int) {
    if (today > dayOfTheWeek.ordinal) {
        when(status) { // DAY OF THE WEEK
            0 -> { // DONE
                Icon(painter = painterResource(id = R.drawable.ic_checkmark_circle_16dp),
                    contentDescription = stringResource(id = R.string.label_completed_exercise),
                    tint = White
                )
            }
            1 -> { // MISSED
                Icon(painter = painterResource(id = R.drawable.ic_times_circle_16dp),
                    contentDescription = stringResource(id = R.string.label_missed_exercise),
                    tint = White
                )
            }
        }
    } else if (today == dayOfTheWeek.ordinal) {
        Icon(painter = painterResource(id = R.drawable.ic_time_clock_16dp),
            contentDescription = stringResource(id = R.string.label_scheduled_exercise),
            tint = White
        )
    } else { // future date (day)
        if (status == WorkoutStatus.SCHEDULED.ordinal ) { // SCHEDULED
            Icon(painter = painterResource(id = R.drawable.ic_alarm_16dp),
                contentDescription =
                stringResource(id = R.string.label_scheduled_exercise),
                tint = MaterialTheme.colorScheme.tertiary
            )
        } else {
            Icon(painter = painterResource(id = R.drawable.ic_dot_16dp),
                contentDescription =
                stringResource(id = R.string.label_future_exercise),
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

    }
}

fun initializeComponentColors(dayOfTheWeek: DaysOfTheWeek, today: Int, status: Int): Triple<Color, Color, Boolean> {
    var textColor: Color = Gray900
    var bgColor: Color = White
    var isScheduleCardEnabled = false

    if (dayOfTheWeek.ordinal < today) { // The past
        textColor = White
        when (status) {
            WorkoutStatus.MISSED.ordinal -> {
                bgColor = WorkoutStatus.MISSED.color
            }
            WorkoutStatus.DONE.ordinal -> {
                bgColor = WorkoutStatus.DONE.color
            }
        }
    } else if (dayOfTheWeek.ordinal == today) { // today
        isScheduleCardEnabled = true
        textColor = White
        bgColor = when (status) {
            WorkoutStatus.DONE.ordinal -> {
                WorkoutStatus.DONE.color
            }
            else -> {
                WorkoutStatus.PENDING.color
            }
        }

    } else { // future
        isScheduleCardEnabled = true
        when (status) {
            WorkoutStatus.PENDING.ordinal -> {
                textColor = White
                bgColor = WorkoutStatus.PENDING.color
            }
            WorkoutStatus.SCHEDULED.ordinal -> {
                textColor = Gray900
                bgColor = White
            }
        }
    }

    return Triple(textColor, bgColor, isScheduleCardEnabled)
}