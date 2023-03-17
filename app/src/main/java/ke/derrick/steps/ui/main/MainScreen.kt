package ke.derrick.steps.ui.main

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ke.derrick.steps.R
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.ui.components.StepsGraph
import ke.derrick.steps.ui.components.Schedule
import ke.derrick.steps.ui.components.StepsGraphHeader
import ke.derrick.steps.ui.components.TopBar
import ke.derrick.steps.utils.getCurrentHourMinute
import kotlin.random.Random

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
                        viewModel.createScheduleReminder(mContext, dayOfTheWeek, mHour, mMinute)
                    }, hourMinute.first, hourMinute.second, false
                ).show()
                dayWithWorkoutStatus[dayOfTheWeek] = WorkoutStatus.SCHEDULED.ordinal
            }

            val yStep = 50
            val points = listOf(150f,100f,250f,200f,330f,300f,90f)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(id = R.dimen.spacing_md))
            ) {
                StepsGraphHeader()
                StepsGraph(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    xValues = (0..6).map { x -> x + 1 },
                    yValues = (0..6).map { y -> (y + 1) * yStep },
                    points = points,
                    midpoint = 3,
                    verticalStep = yStep
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val yStep = 100
    /* to test with fixed points */
    val points = listOf(150f,100f,250f,200f,330f,300f,90f,120f,285f,199f)
    StepsGraph(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        xValues = (0..9).map { x -> x + 1 },
        yValues = (0..6).map { y -> (y + 1) * yStep },
        points = points,
        verticalStep = yStep
    )
}

