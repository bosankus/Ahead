package tech.androidplay.sonali.todo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_ID
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
import tech.androidplay.sonali.todo.view.fragment.TaskEditFragmentArgs
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Feb/2024
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class AlarmForegroundService : Service() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var alarmText = ""
    private var alarmDescription = ""
    private var taskId = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            alarmText = "${intent.extras?.getString(ALARM_TEXT)}"
            alarmDescription = "${intent.extras?.getString(ALARM_DESCRIPTION)}"
            taskId = "${intent.extras?.getString(ALARM_ID)}"

            val uniqueNotificationId = taskId.hashCode()
            val actionIntent = PendingIntent.getActivity(
                this,
                uniqueNotificationId,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val contentArgs = TaskEditFragmentArgs(taskId).toBundle()
            val contentIntent = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.taskEditFragment)
                .setArguments(contentArgs)
                .createPendingIntent()

            createCustomNotification()

            baseNotificationBuilder.apply {
                setContentTitle(alarmText)
                setContentText(alarmDescription)
                setContentIntent(contentIntent)
                setStyle(NotificationCompat.BigTextStyle().bigText(alarmDescription))
                addAction(R.drawable.ic_tick, "Mark Complete", actionIntent)
            }

            notificationManager.notify(uniqueNotificationId, baseNotificationBuilder.build())
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createCustomNotification() {
        val uri = Uri.parse("android.resource://" + this.packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setSound(uri, attributes)
            enableVibration(true)
            setShowBadge(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}
