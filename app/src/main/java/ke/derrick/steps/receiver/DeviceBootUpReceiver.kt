package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ke.derrick.steps.StepsApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DeviceBootUpReceiver: BroadcastReceiver() {
    override fun onReceive(mContext: Context?, mIntent: Intent?) {
        Log.d(TAG, "Device boot up receiver fired. ${mIntent?.action}")
        if (mIntent?.action == "android.intent.action.BOOT_COMPLETED" && mContext != null) {
            resetStepCount(mContext)
        }
    }

    companion object {
        const val TAG = "DeviceBootUpReceiver"
        private var job = Job()
        private val coroutineContext
            get() = job + Dispatchers.IO
        private val scope = CoroutineScope(coroutineContext)

        fun resetStepCount(mContext: Context) = scope.launch {
            (mContext.applicationContext as StepsApplication)
                .repo.persistInitialStepCount(0)
        }


    }
}