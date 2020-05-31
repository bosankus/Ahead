package tech.androidplay.sonali.todo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/31/2020, 5:58 AM
 */

class NotificationManager : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManagerCompat = NotificationManagerCompat.from(context!!)
        val builder = NotificationCompat.Builder(context, "androidplay")
            .setContentTitle("Alarm Notification")
            .setContentText("Wake up Android")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManagerCompat.notify(200, builder.build())
    }

}