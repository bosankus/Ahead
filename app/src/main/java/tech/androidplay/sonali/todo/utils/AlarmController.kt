package tech.androidplay.sonali.todo.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.receiver.AlarmReceiver
import tech.androidplay.sonali.todo.utils.Constants.ALARM_DESCRIPTION
import tech.androidplay.sonali.todo.utils.Constants.ALARM_ID
import tech.androidplay.sonali.todo.utils.Constants.ALARM_TEXT

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Nov/2020
 * Email: ankush@androidplay.in
 */

/** Holds the extension methods for starting & cancelling alarm from Activity/Fragment  */

@ExperimentalCoroutinesApi
fun Context.startAlarmedNotification(
    id: String,
    body: String,
    description: String,
    dateTime: Long,
    alarmManager: AlarmManager
) {
    val intent = Intent(this, AlarmReceiver::class.java).apply {
        this.putExtra(ALARM_TEXT, body)
        this.putExtra(ALARM_DESCRIPTION, description)
        this.putExtra(ALARM_ID, id)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        this,
        id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent)
}

@ExperimentalCoroutinesApi
fun Context.cancelAlarmedNotification(requestCode: String) {
    val intent = Intent(this, AlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(
            this,
            requestCode.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    pendingIntent.cancel()
}
