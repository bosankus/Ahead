package tech.androidplay.sonali.todo.service

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.utils.Notify
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 04/Jan/2021
 * Email: ankush@androidplay.in
 */

/** This class serves the purpose of receiving firebase push notifications with data
 *  and showing them
 *  */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notify: Notify

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.let {
            val title = checkNotNull(it.data["title"].toString())
            val body = checkNotNull(it.data["message"].toString())
            val docId = checkNotNull(it.data["document"].toString())

            logMessage(message.data.toString())

            notify.showNotification(applicationContext) {
                notificationId = docId.hashCode()
                notificationTitle = title
                notificationBody = body
            }
        }
    }
}