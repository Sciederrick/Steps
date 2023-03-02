package ke.derrick.steps.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.R
import ke.derrick.steps.ScheduleStatus
import ke.derrick.steps.ui.theme.Gray900
import ke.derrick.steps.ui.theme.RoundedShapes
import ke.derrick.steps.ui.theme.White
import ke.derrick.steps.ui.utils.getDayOfTheWeek
import ke.derrick.steps.ui.utils.getHourMinute
import kotlin.properties.Delegates

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Schedule() {
    Column(modifier = Modifier.padding(
        vertical = dimensionResource(R.dimen.section_spacing_vertical)
    )) {
        val mContext = LocalContext.current
        Text(text = stringResource(id = R.string.section_schedule_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp))
        Row(modifier = Modifier
            .widthIn()
            .horizontalScroll(rememberScrollState())) {
            val weekStatus by rememberSaveable{ mutableStateOf(hashMapOf<Int, Int>()) }
            weekStatus[0] = 0; weekStatus[1] = 1; weekStatus[2] = 0; weekStatus[3] = 1; weekStatus[4] = 2
            val today = getDayOfTheWeek() - 1
            var status by Delegates.notNull<Int>()
            var bgColor: Color
            var textColor: Color
            var isScheduleCardEnabled = false
            for(dayOfTheWeek in DaysOfTheWeek.values()) {
                status = weekStatus[dayOfTheWeek.ordinal] ?: 3 // 3 is a filler for null
                if (ScheduleStatus.SCHEDULED.ordinal == status) {
                    textColor = White
                    bgColor = ScheduleStatus.SCHEDULED.color
                } else if (ScheduleStatus.DONE.ordinal == status) {
                    textColor = White
                    bgColor = ScheduleStatus.DONE.color
                } else if (ScheduleStatus.MISSED.ordinal == status) {
                    textColor = White
                    bgColor = ScheduleStatus.MISSED.color
                } else {
                    textColor = Gray900
                    bgColor = White
                    isScheduleCardEnabled = true
                }
                val mTime = rememberSaveable { mutableStateOf("") }
                val hourMinute = getHourMinute()
                Card(
                    onClick = {
                        TimePickerDialog(
                            mContext,
                            {_, mHour : Int, mMinute: Int ->
                                mTime.value = "$mHour:$mMinute"
                            }, hourMinute.first, hourMinute.second, false
                        ).show()
                    },
                    enabled = isScheduleCardEnabled,
                    backgroundColor = bgColor,
                    modifier = Modifier.padding(6.dp),
                    shape = RoundedShapes.small) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(54.dp)
                            .padding(vertical = 16.dp)) {
                        Text(text = dayOfTheWeek.shortName, color = textColor)
                        Spacer(modifier = Modifier.size(14.dp))

                        if (today >= dayOfTheWeek.ordinal) {
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
                                2 -> { // SCHEDULED
                                    Icon(painter = painterResource(id = R.drawable.ic_time_clock_16dp),
                                        contentDescription = stringResource(id = R.string.label_scheduled_exercise),
                                        tint = White
                                    )
                                }
                            }
                        } else { // future date (day)
                            Icon(painter = painterResource(id = R.drawable.ic_dot_16dp),
                                contentDescription =
                                stringResource(id = R.string.label_future_exercise),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }


                    }

                }
            }

        }
    }


}