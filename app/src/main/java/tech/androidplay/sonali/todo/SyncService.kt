package tech.androidplay.sonali.todo

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_ID
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_NAME
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_GROUP_ID
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_GROUP_NAME
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_ID
import tech.androidplay.sonali.todo.utils.Constants.START_SYNC_SERVICE
import tech.androidplay.sonali.todo.utils.Constants.STOP_SYNC_SERVICE
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class SyncService : Service() {

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                START_SYNC_SERVICE -> setForegroundService()
                STOP_SYNC_SERVICE -> killForegroundService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killForegroundService() {
        stopForeground(false)
        stopSelf()
    }

    private fun setForegroundService() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= ANDROID_OREO)
            createNotification(notificationManager)

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        notificationManager.notify(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    private fun createNotification(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lightColor = Color.YELLOW
        channel.setShowBadge(true)

        val channelGroup = NotificationChannelGroup(
            NOTIFICATION_GROUP_ID,
            NOTIFICATION_GROUP_NAME
        )

        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannelGroup(channelGroup)
    }
}