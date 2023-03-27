package ke.derrick.steps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ke.derrick.steps.ui.theme.StepsTheme

class StepsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StepsTheme {
                StepsScreen()
            }
        }
    }

    companion object {
        const val TAG = "StepsActivity"
    }
}

@Composable
fun StepsScreen() {
    Box(modifier = Modifier
        .background(MaterialTheme.colorScheme.secondary)
        .fillMaxSize()
        .padding(vertical = 16.dp, horizontal = 12.dp)
        .fillMaxSize()
    ) {
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.TopStart),
            contentPadding = PaddingValues(start = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_left_32dp), contentDescription = null)
            Text(text = "Back", style = MaterialTheme.typography.bodyLarge)
        }

        Column(modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(color = Color.White,
                text = "100", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge)
            Text(color = Color.White,
                text = "STEPS", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStepsScreen() {
    StepsScreen()
}