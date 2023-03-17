package ke.derrick.steps.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ke.derrick.steps.*
import ke.derrick.steps.receiver.SnoozeWorkoutNotificationReceiver
import java.time.LocalTime
import java.util.*


fun getDayOfTheWeek(): Int {
    val c = Calendar.getInstance()
    c.time = Date()
    return c.get(Calendar.DAY_OF_WEEK) // starting from 1
}

fun getCurrentHourMinute() =
    Pair(LocalTime.now().hour, LocalTime.now().minute)

fun makeStepsNotification(mContext: Context, title: String, content: String, scheduledTime: String) {
    val intent = Intent(mContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    val pendingIntent = PendingIntent
        .getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    // Allow system alarm to call our receiver
    val scheduleIntent = Intent(mContext, SnoozeWorkoutNotificationReceiver::class.java).apply {
        putExtra(EXTRA_SCHEDULE_NOTIF_TIME, scheduledTime)
    }
    val snoozePendingIntent = PendingIntent.getBroadcast(
        mContext, BROADCAST_REQUEST_CODE, scheduleIntent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(mContext, SCHEDULE_NOTIF_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText("$scheduledTime, $content")
        .setColor(ContextCompat.getColor(mContext, R.color.blue_800))
        .setColorized(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_snooze_24dp, mContext.getString(R.string.notif_action_snooze_30), snoozePendingIntent)

    with(NotificationManagerCompat.from(mContext.applicationContext)) {
        notify(SCHEDULE_NOTIF_ID, builder.build())
    }
}

fun cancelNotification(mContext: Context, notificationId: Int) {
    NotificationManagerCompat.from(mContext.applicationContext).cancel(notificationId)
}

fun createNotificationChannel(mContext: Context) {
    val name = mContext.getString(R.string.notif_schedule_channel_name)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val mChannel = NotificationChannel(SCHEDULE_NOTIF_CHANNEL_ID, name, importance)
    // Register the channel with the system.
    val notificationManager
        = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(mChannel)
}

fun convertToTwoDigitNumberString(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

