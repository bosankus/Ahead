package tech.androidplay.sonali.todo.workers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.Notifier.dismissNotification
import tech.androidplay.sonali.todo.utils.Notifier.show
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.startAlarmedNotification
import tech.androidplay.sonali.todo.view.activity.MainActivity
import tech.androidplay.sonali.todo.workers.TaskImageUploadWorker.Companion.UPLOADED_IMAGE_URI

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Feb/2021
 * Email: ankush@androidplay.in
 */

/**
 * Creates only Image tasks in the background
 */

@ExperimentalCoroutinesApi
@HiltWorker
class TaskCreationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val alarmManager: AlarmManager
) :
    CoroutineWorker(context, workerParameters) {

    private val context = applicationContext
    private val repository = TodoRepository()
    private val currentUser = repository.userDetails

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        val taskCreator = checkNotNull(currentUser?.uid)
        val taskBody = checkNotNull(inputData.getString(TASK_BODY))
        val taskDesc = checkNotNull(inputData.getString(TASK_DESC))
        val taskDate = checkNotNull(inputData.getString(TASK_DATE))
        val taskAssigneeList = (inputData.getStringArray(TASK_ASSIGNEE))?.toList()
        val imageUri = inputData.getString(UPLOADED_IMAGE_URI)

        val taskMap = hashMapOf(
            "creator" to taskCreator,
            "todoBody" to taskBody,
            "todoDesc" to taskDesc,
            "todoDate" to taskDate,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isCompleted" to false,
            "taskImage" to imageUri,
            "assignee" to taskAssigneeList,
            "priority" to 1,
        )

        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val response: String = repository.createTaskUsingWorker(taskMap)
        if (response.isNotEmpty()) {
            context.startAlarmedNotification(
                id = response,
                taskBody,
                taskDesc,
                taskDate.toLong(),
                alarmManager
            )
            showNotification(taskBody, context.getString(R.string.task_added_success), intent)
            Result.success()
        } else {
            showNotification(taskBody, context.getString(R.string.task_added_failure), intent)
            Result.failure()
        }
    }

    private fun showNotification(title: String, caption: String, intent: Intent) {
        dismissNotification(context, title.hashCode())

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        show(context) {
            contentTitle = title
            contentText = caption
            this.pendingIntent = pendingIntent
            notificationId = title.hashCode()
        }
    }

    companion object {
        const val TASK_OBJ = "task_object"
        const val TASK_BODY = "TASK_BODY"
        const val TASK_DESC = "TASK_DESC"
        const val TASK_DATE = "TASK_DATE"
        const val TASK_ASSIGNEE = "TASK_ASSIGNEE"
        const val TASK_IMAGE_URI = "TASK_IMAGE_URI"
    }
}