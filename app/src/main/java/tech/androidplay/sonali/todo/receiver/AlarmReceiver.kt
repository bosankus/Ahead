package tech.androidplay.sonali.todo.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.service.TaskStatusUpdateService
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_ID
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_ID
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_NAME
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_ID
import tech.androidplay.sonali.todo.view.fragment.TaskEditFragmentArgs
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Oct/2020
 * Email: ankush@androidplay.in
 */

/** A [HiltBroadcastReceiver] receiver to send notification of task triggered by AlarmManager */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var alarmText = ""
    private var alarmDescription = ""
    private var taskId = ""

    /**
     * if device reboots then the intent extras will be null at that time, so we cant
     * start alarm with null values. In that case we need to fetch the data from a permanent source,
     * i.e SQL DB that means Room DB.
     * Execution: Start an alarm > save the alarm details in Room DB > when device reboots, again
     * set the alarm with the details from DB > may be we can use a work manager for that when
     * device reboots./ or a foreground service.
     * while setting the alarm always check if the alarm time is ahead of current time.
     *
     * As per SOLID - Create a dedicated class for setting alarm/ cancelling alarm
     */


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if ((intent?.action == ALARM_INTENT_ACTION
                    || intent?.action == "android.intent.action.BOOT_COMPLETED")
            && context != null
        ) {
            alarmText = "${intent.extras?.get(ALARM_TEXT)}"
            alarmDescription = "${intent.extras?.get(ALARM_DESCRIPTION)}"
            taskId = "${intent.extras?.get(ALARM_ID)}"

            showNotification(context, alarmText, alarmDescription, taskId)
        } else return
    }


    private fun showNotification(
        context: Context,
        alarmText: String,
        alarmDescription: String,
        taskId: String
    ) {

        // Generates notification code taskId.hashCode()
        val uniqueNotificationId = taskId.hashCode()
        // Extras for notification action button
        val intent = Intent(context, TaskStatusUpdateService::class.java).also {
            it.putExtra(ALARM_ID, taskId)
            it.putExtra(NOTIFICATION_ID, uniqueNotificationId)
        }

        val actionIntent = PendingIntent.getService(
            context, uniqueNotificationId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contentArgs = TaskEditFragmentArgs(taskId).toBundle()
        val contentIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskEditFragment)
            .setArguments(contentArgs)
            .createPendingIntent()

        // Creating notification channel
        if (Constants.DEVICE_ANDROID_VERSION >= Constants.ANDROID_OREO)
            createNotification(context, notificationManager)

        baseNotificationBuilder.apply {
            setContentTitle(alarmText)
            setContentText(alarmDescription)
            setContentIntent(contentIntent)
            setStyle(NotificationCompat.BigTextStyle().bigText(alarmDescription))
            addAction(R.drawable.ic_notification, "Mark Complete", actionIntent)
        }

        notificationManager.notify(uniqueNotificationId, baseNotificationBuilder.build())
    }

    /**
     * This method creates the notification channel with custom sound attribute
     */
    private fun createNotification(context: Context, notificationManager: NotificationManager) {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setSound(uri, attributes)
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val ALARM_INTENT_ACTION = "alarm_broadcast_action"
    }
}