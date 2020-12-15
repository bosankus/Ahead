package tech.androidplay.sonali.todo.utils.alarmutils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Nov/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
fun Fragment.startAlarmedNotification(
    requestCode: String,
    notificationText: String,
    notificationBody: String,
    dateTime: Long,
    alarmManager: AlarmManager
) {
    val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
        this.putExtra(ALARM_TEXT, notificationText)
        this.putExtra(ALARM_DESCRIPTION, notificationBody)
        this.putExtra(TASK_DOC_ID, requestCode)
    }
    val pendingIntent =
        PendingIntent.getBroadcast(
            requireContext(),
            requestCode.generateRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        dateTime,
        pendingIntent
    )
}

fun Fragment.cancelAlarmedNotification(requestCode: String) {
    val intent = Intent(requireContext(), AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            requireContext(),
            requestCode.generateRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    pendingIntent.cancel()
}

fun View.cancelAlarmedNotification(requestCode: String) {
    val intent = Intent(this.context, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            this.context,
            requestCode.generateRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    pendingIntent.cancel()
}

@Suppress("UNUSED_CHANGED_VALUE")
private fun String.generateRequestCode(): Int {
    var ascii: Int
    var code = 0
    for (i in this.indices - 1) {
        ascii = this[i].toInt()
        code += ascii
    }
    return code
}
