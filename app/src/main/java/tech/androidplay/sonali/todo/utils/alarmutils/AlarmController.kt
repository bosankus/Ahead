package tech.androidplay.sonali.todo.utils.alarmutils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Nov/2020
 * Email: ankush@androidplay.in
 */

fun Fragment.startAlarmedNotification(
    requestCode: Int,
    notificationText: String,
    notificationBody: String,
    dateTime: Long,
    alarmManager: AlarmManager
) {
    val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
        this.putExtra(ALARM_TEXT, notificationText)
        this.putExtra(ALARM_DESCRIPTION, notificationBody)
    }
    val pendingIntent =
        PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        dateTime,
        pendingIntent
    )
}

fun Fragment.cancelAlarmedNotification(requestCode: Int) {
    val intent = Intent(requireContext(), AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    pendingIntent.cancel()
}

fun View.cancelAlarmedNotification(requestCode: Int) {
    val intent = Intent(this.context, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            this.context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    pendingIntent.cancel()
}
