package tech.androidplay.sonali.todo.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.Notifier.dismissNotification
import tech.androidplay.sonali.todo.utils.Notifier.show
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.startAlarmedNotification
import tech.androidplay.sonali.todo.view.activity.MainActivity
import tech.androidplay.sonali.todo.workers.TaskImageUploadWorker.Companion.UPLOADED_IMAGE_URI

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Feb/2021
 * Email: ankush@androidplay.in
 */
@ExperimentalCoroutinesApi
class TaskCreationWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val context = applicationContext
    private val repository = TodoRepository()
    private val currentUser = repository.userDetails

    override suspend fun doWork(): Result {
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

        // TODO: add deep link, deal detail fragment, then take user to there from notification.
        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        when (repository.createTask(taskMap)) {
            is ResultData.Loading -> {/*Do Nothing*/
            }
            is ResultData.Success -> {
                // TODO: Create alarm
                /*context.startAlarmedNotification(it.data)*/
                logMessage("$inputData")
                showNotification(taskBody, context.getString(R.string.task_added_success), intent)
                return Result.success()
            }
            is ResultData.Failed -> {
                showNotification(taskBody, context.getString(R.string.task_added_failure), intent)
                return Result.failure()
            }
            else -> Result.failure()
        }

        return Result.failure()
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
        const val TASK_BODY = "TASK_BODY"
        const val TASK_DESC = "TASK_DESC"
        const val TASK_DATE = "TASK_DATE"
        const val TASK_ASSIGNEE = "TASK_ASSIGNEE"
        const val TASK_IMAGE_URI = "TASK_IMAGE_URI"
    }
}