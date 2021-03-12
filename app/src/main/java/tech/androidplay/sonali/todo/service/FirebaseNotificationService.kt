package tech.androidplay.sonali.todo.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.utils.Notifier.show
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

            // sending the notification
            show(applicationContext) {
                notificationId = docId.hashCode()
                contentTitle = title
                contentText = body
            }
        }
    }
}