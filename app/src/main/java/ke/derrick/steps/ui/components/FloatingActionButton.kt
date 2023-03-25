package ke.derrick.steps.ui.components

import android.content.Intent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import ke.derrick.steps.R
import ke.derrick.steps.service.StepsTrackerService

@Composable
fun FloatingActionButton() {
    val mContext = LocalContext.current
    val intent = Intent(mContext, StepsTrackerService::class.java)
    Button(
        onClick = {
            mContext.applicationContext.startForegroundService(intent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
        Icon(painter = painterResource(id = R.drawable.ic_walk_32dp), contentDescription = null)
        Text(text = "Start", style = MaterialTheme.typography.headlineMedium)
    }
}