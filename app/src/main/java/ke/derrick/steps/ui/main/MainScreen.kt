package ke.derrick.steps.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ke.derrick.steps.R
import ke.derrick.steps.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(topBar = { TopBar() }, modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}