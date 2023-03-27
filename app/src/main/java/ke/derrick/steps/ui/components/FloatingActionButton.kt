package ke.derrick.steps.ui.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ke.derrick.steps.MainActivity
import ke.derrick.steps.R
import ke.derrick.steps.service.StepsTrackerService

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun FAB() {
    val mContext = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            StepsTrackerService.startService(mContext)
            Log.d(MainActivity.TAG, "Permission: Granted")
        } else {
            Log.d(MainActivity.TAG, "Permission: Denied")
        }
    }
    Button(
        onClick = {
            when {
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACTIVITY_RECOGNITION)
                        == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is granted
                    Log.d(MainActivity.TAG, "permission granted")
                    StepsTrackerService.startService(mContext)
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    mContext as Activity,
                    Manifest.permission.ACTIVITY_RECOGNITION
                )
                -> {
                    // Additional rationale should be displayed
                    Log.d(MainActivity.TAG, "additional rationale should be displayed")
                    requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }
                else -> {
                    // Permission has not been asked yet
                    Log.d(MainActivity.TAG, "permission has not been asked yet")
                    requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }

            }

        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
        Icon(painter = painterResource(id = R.drawable.ic_walk_32dp), contentDescription = null)
        Text(text = "Start", style = MaterialTheme.typography.headlineMedium)
    }
}