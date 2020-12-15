package tech.androidplay.sonali.todo.utils.alarmutils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.di.HiltBroadcastReceiver
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Oct/2020
 * Email: ankush@androidplay.in
 */

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

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let {
            alarmText = "${it.extras?.get(ALARM_TEXT)}"
            alarmDescription = "${it.extras?.get(ALARM_DESCRIPTION)}"
            taskId = "${it.extras?.get(TASK_DOC_ID)}"
        } ?: return
        context?.let {
            showNotification(it, alarmText, alarmDescription, taskId)
        }
    }


    private fun showNotification(
        context: Context,
        alarmText: String,
        alarmDescription: String,
        taskId: String
    ) {
        val intent = Intent(context, NotificationReceiver::class.java).also {
            it.putExtra(TASK_DOC_ID, taskId)
            it.putExtra("notificationId", NOTIFICATION_ID)
        }
        val pendingIntent = PendingIntent.getService(
            context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        /*val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager*/

        if (Build.VERSION.SDK_INT >= ANDROID_OREO)
            createNotification(context, notificationManager)

        // Notification button
        baseNotificationBuilder.apply {
            setContentTitle(alarmText)
            setContentText(alarmDescription)
            setStyle(NotificationCompat.BigTextStyle().bigText(alarmDescription))
            addAction(R.drawable.ic_notification, "Mark Complete", pendingIntent)
        }

        notificationManager.notify(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun createNotification(context: Context, notificationManager: NotificationManager) {
        val uri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setSound(uri, attributes)
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}