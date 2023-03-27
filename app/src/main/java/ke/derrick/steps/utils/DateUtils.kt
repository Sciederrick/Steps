package ke.derrick.steps.utils

import android.app.Notification
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

fun makeStepsReminderNotification(mContext: Context, title: String, content: String, scheduledTime: String) {
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
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_snooze_24dp, mContext.getString(R.string.notif_action_snooze_30), snoozePendingIntent)

    with(NotificationManagerCompat.from(mContext.applicationContext)) {
        notify(SCHEDULE_NOTIF_ID, builder.build())
    }
}

fun makeStepsOngoingNotification(mContext: Context): Notification {
//    val intent = Intent(mContext, )
    val notification = NotificationCompat.Builder(mContext, ONGOING_NOTIF_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(mContext.getString(R.string.notif_ongoing_title))
        .setContentText(mContext.getString(R.string.notif_ongoing_text))
        .setSubText(mContext.getString(R.string.notif_ongoing_subtext))
        .setColor(ContextCompat.getColor(mContext, R.color.blue_800))
        .setColorized(true)
        .setOnlyAlertOnce(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        // Enable launching the player by clicking the notification
//        .setContentIntent(pendingIntent)
    // Stop the service when the notification is swiped away
//    setDeleteIntent(
//        MediaButtonReceiver.buildMediaButtonPendingIntent(
//            context,
//            PlaybackStateCompat.ACTION_STOP
//        )
//    )
//        .addAction(R.drawable.ic_pause_24dp, mContext.getString(R.string.notif_action_pause), snoozePendingIntent)
    // Take advantage of MediaStyle features
//        .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()
//        .setShowActionsInCompactView(0)

        // Add a cancel button
//        .setShowCancelButton(true)
//        .setCancelButtonIntent(
//            MediaButtonReceiver.buildMediaButtonPendingIntent(
//                context,
//                PlaybackStateCompat.ACTION_STOP
//            )
//        )
//    )

    with(NotificationManagerCompat.from(mContext.applicationContext)) {
        notify(ONGOING_NOTIF_ID, notification.build())
    }

    return notification.build()
}

fun cancelNotification(mContext: Context, notificationId: Int) {
    NotificationManagerCompat.from(mContext.applicationContext).cancel(notificationId)
}

fun createNotificationChannels(mContext: Context) {
    val name = mContext.getString(R.string.notif_schedule_channel_name)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val mChannel = NotificationChannel(SCHEDULE_NOTIF_CHANNEL_ID, name, importance)

    val name2 = mContext.getString(R.string.notif_ongoing_channel_name)
    val mChannel2 = NotificationChannel(ONGOING_NOTIF_CHANNEL_ID, name2, importance)
    // Register the channel with the system.
    val notificationManager
        = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannels(listOf(mChannel, mChannel2))
}

fun convertToTwoDigitNumberString(number: Int): String {
    return if (number < 10) {
        "0$number"
    } else {
        number.toString()
    }
}

