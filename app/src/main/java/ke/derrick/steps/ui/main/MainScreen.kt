package ke.derrick.steps.ui.main

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ke.derrick.steps.CACHE_SIZE
import ke.derrick.steps.R
import ke.derrick.steps.WorkoutStatus
import ke.derrick.steps.data.local.entities.Steps
import ke.derrick.steps.ui.components.*
import ke.derrick.steps.util.convertToTwoDigitNumberString
import ke.derrick.steps.util.fillGapPoints
import ke.derrick.steps.util.fillWithDefaultPoints
import ke.derrick.steps.util.getCurrentHourMinute
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(factory = MainViewModel.provideFactory())) {
    Scaffold(
        topBar = { TopBar() },
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
        floatingActionButton = { FAB() }
    ) {
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

            var cachedPoints by rememberSaveable { mutableStateOf(emptyList<Triple<Float, String, Long>>()) }
            var startId by rememberSaveable { mutableStateOf(0L) }

            var stepsList by rememberSaveable { mutableStateOf<List<Steps?>>(emptyList()) }
            LaunchedEffect(startId) {
//                stepsList = viewModel.getStepCountAsync(startId, CACHE_SIZE).await() ?: emptyList()
                stepsList = viewModel.getStepCountFakeAsync(startId, CACHE_SIZE).await()
                Log.d("MainScreen", "steps list: $stepsList")

                cachedPoints = if (stepsList.isNotEmpty() && stepsList.size >= 7) { // values from the DB
                    stepsList.map { steps ->
                        Triple(steps!!.count.toFloat(), steps.day, steps.id)
                    }
                } else if(stepsList.isEmpty()) {
                    fillWithDefaultPoints(7)
                } else {
                    fillGapPoints(7, stepsList)
                }

            }


            Log.d("MainScreen", "cached points: $cachedPoints")
            if (cachedPoints.size >= 7) {
                GraphSection(cachedPoints = cachedPoints) { id -> startId = id }
            }


            StatsCards()
            
            Spacer(modifier = Modifier.size(100.dp))
        }

    }

}

@Composable
fun GraphSection(cachedPoints: List<Triple<Float, String, Long>>, fireLoadingHint: (Long) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.spacing_md))
    ) {
        val yStep = 50
        val midpoint = 3
        val numPoints = 7
        val numY = 7
        val loadingHintThreshold = 14

        var start by remember { mutableStateOf(cachedPoints.size - numPoints) }
        val points by remember(start) { mutableStateOf(cachedPoints.subList(start, start + numPoints)) }

        StepsGraphHeader(points[3].first.toInt())
        StepsGraph(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            xLabels = points.map { pair -> pair.second },
            xValues = (0 until numPoints).map { x -> x + 1 },
            yValues = (0 until numY).map { y -> (y + 1) * yStep },
            points = points.map { pair -> pair.first },
            midpoint = midpoint,
            verticalStep = yStep
        ) { offset ->
            if (start - offset < 0)
                start = 0
            else if (start - offset > cachedPoints.size - numPoints)
                start = cachedPoints.size - numPoints
            else {
                if (abs(offset) != 0) start -= (offset/abs(offset))
                if (start <= loadingHintThreshold || start >= cachedPoints.size - loadingHintThreshold) {
                    // Calculate the value of the next starting id and pass it to the fireLoadingHint event
                    // TODO: Test pagination
                    if (start < (start/2)) {
                        fireLoadingHint(cachedPoints[0].third - CACHE_SIZE)
                    } else {
                        fireLoadingHint(cachedPoints[0].third + CACHE_SIZE)
                    }
                }
            }

        }
    }
}



