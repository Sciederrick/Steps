package ke.derrick.steps.ui.main

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Shapes
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ke.derrick.steps.DaysOfTheWeek
import ke.derrick.steps.R
import ke.derrick.steps.ScheduleStatus
import ke.derrick.steps.ui.components.Schedule
import ke.derrick.steps.ui.components.TopBar
import ke.derrick.steps.ui.theme.Gray900
import ke.derrick.steps.ui.theme.Purple40
import ke.derrick.steps.ui.theme.RoundedShapes
import ke.derrick.steps.ui.theme.White
import ke.derrick.steps.ui.utils.getDayOfTheWeek
import java.time.Instant
import kotlin.properties.Delegates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(topBar = { TopBar() },
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 12.dp)) {
        Column(modifier = Modifier
            .padding(paddingValues = it)
            .verticalScroll(rememberScrollState())) {
            Schedule()
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}

