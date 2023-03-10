package ke.derrick.steps

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.AlarmManagerCompat
import androidx.navigation.compose.rememberNavController
import ke.derrick.steps.receiver.ScheduleReceiver
import ke.derrick.steps.ui.splash.SplashScreen
import ke.derrick.steps.ui.theme.StepsTheme

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
