package tech.androidplay.sonali.todo.utils.alarmutils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import tech.androidplay.sonali.todo.TodoApplication
import tech.androidplay.sonali.todo.utils.Constants
import java.util.*

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
    calendar: Calendar,
    alarmManager: AlarmManager
) {
    val year = TodoApplication.GLOBAL_YEAR
    val day = TodoApplication.GLOBAL_DAY
    val month = TodoApplication.GLOBAL_MONTH
    val minute = TodoApplication.GLOBAL_MINUTE
    val hour = TodoApplication.GLOBAL_HOUR
    val second = TodoApplication.GLOBAL_SECOND

    val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
        this.putExtra(Constants.ALARM_TEXT, notificationText)
        this.putExtra(Constants.ALARM_DESCRIPTION, notificationBody)
    }
    val pendingIntent =
        PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    calendar.set(year, month, day, hour, minute, second)
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
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
