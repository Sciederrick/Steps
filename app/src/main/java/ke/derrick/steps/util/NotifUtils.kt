package ke.derrick.steps.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ke.derrick.steps.*
import ke.derrick.steps.receiver.SnoozeWorkoutNotificationReceiver
import ke.derrick.steps.service.StepsTrackerService

class NotifUtils(private val mContext: Context) {
    private val startActivityIntent = Intent(mContext, StepsActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    private val goToStepsActivity: PendingIntent = PendingIntent
        .getActivity(mContext, 0, startActivityIntent, PendingIntent.FLAG_IMMUTABLE)

    private val stopServiceIntent = Intent(mContext, StepsTrackerService::class.java).apply {
        putExtra(IsServiceStart.SERVICE_STOP.title, IsServiceStart.SERVICE_STOP.ordinal)
    }
    private val stopService: PendingIntent = PendingIntent.getForegroundService(mContext,
        0, stopServiceIntent, PendingIntent.FLAG_IMMUTABLE)

    private val notifOngoingBuilder = NotificationCompat.Builder(mContext, ONGOING_NOTIF_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(mContext.getString(R.string.notif_ongoing_title))
        .setContentText(mContext.getString(R.string.notif_ongoing_text))
        .setSubText(mContext.getString(R.string.notif_ongoing_subtext))
        .setColor(ContextCompat.getColor(mContext, R.color.blue_800))
        .setColorized(true)
        .setOnlyAlertOnce(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentIntent(goToStepsActivity)
        .setDeleteIntent(stopService)    // Stop the service when the notification is swiped away
        .addAction(R.drawable.ic_stop_24dp, mContext.getString(R.string.notif_action_stop), stopService)
        .setStyle( // Take advantage of MediaStyle features
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0)
                .setShowCancelButton(true)
                .setCancelButtonIntent(stopService)
        )

    fun makeStepsReminderNotification(title: String, content: String, scheduledTime: String) {
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

    fun makeStepsOngoingNotification(): Notification {
        with(NotificationManagerCompat.from(mContext.applicationContext)) {
            notify(ONGOING_NOTIF_ID, notifOngoingBuilder.build())
        }

        return notifOngoingBuilder.build()
    }

    fun updateStepsOngoingNotification(numSteps: String) {
        notifOngoingBuilder.setContentText(numSteps)
        with(NotificationManagerCompat.from(mContext.applicationContext)) {
            notify(ONGOING_NOTIF_ID, notifOngoingBuilder.build())
        }
    }

    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(mContext.applicationContext).cancel(notificationId)
    }

    fun createNotificationChannels() {
        val name = mContext.getString(R.string.notif_schedule_channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(SCHEDULE_NOTIF_CHANNEL_ID, name, importance)

        val name2 = mContext.getString(R.string.notif_ongoing_channel_name)
        val mChannel2 = NotificationChannel(ONGOING_NOTIF_CHANNEL_ID, name2, importance)
        // Register the channel with the system.
        val notificationManager
                = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(listOf(mChannel, mChannel2))
    }
}