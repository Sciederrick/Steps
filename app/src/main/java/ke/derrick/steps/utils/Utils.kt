package ke.derrick.steps.utils

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import ke.derrick.steps.CHANNEL_ID
import ke.derrick.steps.R
import java.time.LocalTime
import java.util.*


fun getDayOfTheWeek(): Int {
    val c = Calendar.getInstance()
    c.time = Date()
    return c.get(Calendar.DAY_OF_WEEK) // starting from 1
}

fun getCurrentHourMinute() =
    Pair(LocalTime.now().hour, LocalTime.now().minute)

fun makeStepsNotification(mContext: Context, title: String, content: String) {
    var builder = NotificationCompat.Builder(mContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_MAX)
}
