//package ke.derrick.steps.utils
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import ke.derrick.steps.MainActivity
//
//class PermissionUtils(private val mContext: Context) {
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            Log.i(MainActivity.TAG, "Permission: Granted")
//        } else {
//            Log.i(MainActivity.TAG, "Permission: Denied")
//        }
//    }
//
//
//    private fun requestPermission() {
//        when {
//            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
//                    == PackageManager.PERMISSION_GRANTED -> {
//                // Permission is granted
//                Log.i(MainActivity.TAG, "permission granted")
//            }
//            ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACTIVITY_RECOGNITION
//            )
//            -> {
//                // Additional rationale should be displayed
//                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
//            }
//            else -> {
//                // Permission has not been asked yet
//                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
//            }
//
//        }
//    }
//}