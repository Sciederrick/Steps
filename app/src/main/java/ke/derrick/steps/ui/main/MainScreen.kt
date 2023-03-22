package ke.derrick.steps.ui.main

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
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
            LaunchedEffect(true) {
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

            val cachedPoints = (0..50).map { n ->
                var num = Random.nextInt(350)
                if (num <= 50)
                    num += 100
                 Pair(num.toFloat(), if (n > 31) n -31 else n)
            }
            Log.d("MainScreen", "$cachedPoints")
            Log.d("MainScreen", "upper recomposition")

            GraphSection(cachedPoints = cachedPoints)

        }

    }

}

@Composable
fun GraphSection(cachedPoints: List<Pair<Float, Int>>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.spacing_md))
    ) {
        val yStep = 50
        val midpoint = 3

        var start by remember { mutableStateOf(cachedPoints.size - 7) }
        val points by remember(start) { mutableStateOf(cachedPoints.subList(start, start + 7)) }


        Log.d("MainScreen", "recomposition")
        StepsGraphHeader(points[3].first.toInt())
        StepsGraph(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            xLabels = points.map { pair -> pair.second },
            xValues = (0..6).map { x -> x + 1 },
            yValues = (0..6).map { y -> (y + 1) * yStep },
            points = points.map { pair -> pair.first},
            midpoint = midpoint,
            verticalStep = yStep
        ) { offset ->
            if (start - offset < 0)
                start = 0
            else if (start - offset > cachedPoints.size - 7)
                start = cachedPoints.size - 7
            else
                start -= offset
            Log.d("MainScreen", "start: $start")
        }
    }
}



