package ke.derrick.steps.ui.main

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.ui.components.Schedule
import ke.derrick.steps.ui.components.TopBar
import ke.derrick.steps.utils.getCurrentHourMinute

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(factory = MainViewModel.provideFactory())) {
    Scaffold(topBar = { TopBar() },
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 12.dp)) {
        val mContext = LocalContext.current
        Column(modifier = Modifier
            .padding(paddingValues = it)
            .verticalScroll(rememberScrollState())) {
            var dayWithWorkoutStatus by rememberSaveable{ mutableStateOf<Array<Int>>(emptyArray()) }
            LaunchedEffect(dayWithWorkoutStatus) {
                dayWithWorkoutStatus = viewModel.getSevenDayWorkoutStatusAsync().await()
            }

            val hourMinute = getCurrentHourMinute()
            Schedule(dayWithWorkoutStatus) { dayOfTheWeek ->
                TimePickerDialog(
                    mContext,
                    {_, mHour : Int, mMinute: Int ->
                        viewModel.createScheduleReminder(dayOfTheWeek, mHour, mMinute)
                    }, hourMinute.first, hourMinute.second, false
                ).show()
                dayWithWorkoutStatus[dayOfTheWeek] = WorkoutStatus.SCHEDULED.ordinal
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}

