package ke.derrick.steps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ke.derrick.steps.utils.makeStepsNotification

class ScheduleReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
//        makeStepsNotification(p0, "Reminder", "")
        Toast.makeText(p0, "alarm received", Toast.LENGTH_LONG).show()
    }
}