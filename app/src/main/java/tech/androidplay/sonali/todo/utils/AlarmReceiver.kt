package tech.androidplay.sonali.todo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.di.HiltBroadcastReceiver
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_ID
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Oct/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var pendingIntent: PendingIntent

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let {
            alarmText = it.extras?.get(ALARM_TEXT).toString()
            alarmDescription = it.extras?.get(ALARM_DESCRIPTION).toString()
        }
        showNotification(context, alarmText, alarmDescription)
    }

    private fun showNotification(context: Context?, alarmText: String, alarmDescription: String) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= ANDROID_OREO)
            createNotification(notificationManager)

        notificationBuilder.apply {
            setContentTitle(alarmText)
            setContentText(alarmDescription)
            addAction(R.drawable.ic_snooze, "Stop", pendingIntent)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotification(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lightColor = Color.YELLOW
            setShowBadge(true)

        }

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        var alarmText = ""
        var alarmDescription = ""
    }
}