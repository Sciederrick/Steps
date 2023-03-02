package ke.derrick.steps.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ke.derrick.steps.ui.components.Schedule
import ke.derrick.steps.ui.components.TopBar

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

