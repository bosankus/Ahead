package tech.androidplay.sonali.todo.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.DEVICE_ANDROID_VERSION
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_ID
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_CHANNEL_NAME
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.generateRequestCode
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 04/Jan/2021
 * Email: ankush@androidplay.in
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.let {
            val title = checkNotNull(it.data["title"].toString())
            val body = checkNotNull(it.data["message"].toString())
            val docId = checkNotNull(it.data["document"].toString())
            logMessage(docId)
            sendNotification(title, body, docId)
        }
    }

    private fun sendNotification(title: String, body: String, taskId: String) {
        val uniqueNotificationId = taskId.generateRequestCode()

        if (DEVICE_ANDROID_VERSION >= ANDROID_OREO)
            createNotification(notificationManager)

        baseNotificationBuilder.apply {
            setContentTitle(title)
            setContentText(body)
            setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        notificationManager.notify(uniqueNotificationId, baseNotificationBuilder.build())
    }

    private fun createNotification(notificationManager: NotificationManager) {
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.noti_sound)

        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
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