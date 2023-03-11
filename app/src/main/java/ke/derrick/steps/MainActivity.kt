package ke.derrick.steps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ke.derrick.steps.ui.splash.SplashScreen
import ke.derrick.steps.ui.theme.StepsTheme
import ke.derrick.steps.utils.createNotificationChannel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StepsTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showLandingScreen by remember { mutableStateOf(true) }
                    if (showLandingScreen) {
                        SplashScreen(onTimeout = { showLandingScreen = false })
                    } else {
                        StepsNavHost(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
