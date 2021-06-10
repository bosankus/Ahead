package tech.androidplay.sonali.todo.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.Constants.ALARM_ID
import tech.androidplay.sonali.todo.utils.Constants.NOTIFICATION_ID
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Dec/2020
 * Email: ankush@androidplay.in
 */

/** This service class serves the purpose of notification action button click listener to mark
 *  task as completed. Calls the 'updateTask' method from [TodoRepository]
 * */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskStatusUpdateService : Service() {

    @Inject
    lateinit var repository: TodoRepository

    @Inject
    lateinit var notificationManager: NotificationManager

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var taskId = ""
    private var notificationId = 0

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            taskId = "${it.getStringExtra(ALARM_ID)}"
            notificationId = it.getIntExtra(NOTIFICATION_ID, 0)
            notificationManager.cancel(notificationId)
        }
        changeTaskStatus(taskId)
        return START_REDELIVER_INTENT
    }

    private fun changeTaskStatus(taskId: String) {
        val map: Map<String, Boolean> = mapOf("isCompleted" to true)
        scope.launch { repository.updateTask(taskId, map) }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}