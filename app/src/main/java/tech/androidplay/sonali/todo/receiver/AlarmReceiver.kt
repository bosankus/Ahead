package tech.androidplay.sonali.todo.receiver

import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.service.AlarmForegroundService

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 07/Oct/2020
 * Email: ankush@androidplay.in
 */

/** A [HiltBroadcastReceiver] receiver to send notification of task triggered by AlarmManager */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AlarmReceiver : HiltBroadcastReceiver() {

    /**
     * if device reboots then the intent extras will be null at that time, so we cant
     * start alarm with null values. In that case we need to fetch the data from a permanent source,
     * i.e SQL DB that means Room DB.
     * Execution: Start an alarm > save the alarm details in Room DB > when device reboots, again
     * set the alarm with the details from DB > may be we can use a work manager for that when
     * device reboots./ or a foreground service.
     * while setting the alarm always check if the alarm time is ahead of current time.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if ((intent?.action == ALARM_INTENT_ACTION
                    || intent?.action == "android.intent.action.BOOT_COMPLETED")
            && context != null
        ) {
            val serviceIntent = Intent(context, AlarmForegroundService::class.java).apply {
                putExtras(intent)
            }
            context.startForegroundService(serviceIntent)
        } else return
    }

    companion object {
        const val ALARM_INTENT_ACTION = "alarm_broadcast_action"
    }
}