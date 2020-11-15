package tech.androidplay.sonali.todo.utils.alarmutils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_DAY
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_HOUR
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_MINUTE
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_MONTH
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_SECOND
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_YEAR
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
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
    val year = GLOBAL_YEAR
    val day = GLOBAL_DAY
    val month = GLOBAL_MONTH
    val minute = GLOBAL_MINUTE
    val hour = GLOBAL_HOUR
    val second = GLOBAL_SECOND

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
