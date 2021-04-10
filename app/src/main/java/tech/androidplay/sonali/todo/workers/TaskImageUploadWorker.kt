package tech.androidplay.sonali.todo.workers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.Notifier.dismissNotification
import tech.androidplay.sonali.todo.utils.Notifier.progressable
import tech.androidplay.sonali.todo.utils.Notifier.show
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.view.activity.MainActivity
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_BODY
import tech.androidplay.sonali.todo.workers.TaskCreationWorker.Companion.TASK_IMAGE_URI
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 13/Feb/2021
 * Email: ankush@androidplay.in
 */

/**
 * Uploads the image of image task only
 */

@ExperimentalCoroutinesApi
class TaskImageUploadWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val context = applicationContext
    private val repository = TodoRepository()
    private val taskBody: String = checkNotNull(inputData.getString(TASK_BODY))


    override suspend fun doWork(): Result {
        val fileUri = inputData.getString(TASK_IMAGE_URI)?.toUri()
        fileUri?.let { return uploadImageFromUri(fileUri) }
        throw IllegalStateException("Image Uri doesn't exist")
    }


    private suspend fun uploadImageFromUri(fileUri: Uri): Result = suspendCoroutine { cont ->
        repository.uploadImageFromWorker(fileUri) { result, percentage ->
            when (result) {
                is ResultData.Loading -> {
                    showProgressNotification(
                        context.getString(R.string.task_progress_uploading), percentage
                    )
                }
                is ResultData.Success -> {
                    showUploadFinishedNotification(result.data)

                    val data = Data.Builder()
                        .putAll(inputData)
                        .putString(UPLOADED_IMAGE_URI, result.data.toString())

                    cont.resume(Result.success(data.build()))
                }
                is ResultData.Failed -> {
                    showUploadFinishedNotification(null)
                    cont.resume(Result.failure())
                }
            }
        }
    }


    private fun showProgressNotification(caption: String, percent: Int) {
        progressable(context, 100, percent) {
            contentTitle = taskBody
            contentText = caption
            smallIcon = R.drawable.ic_notification
            notificationId = taskBody.hashCode()
        }
    }


    private fun showUploadFinishedNotification(downloadUri: Uri?) {
        dismissNotification(context, taskBody.hashCode())

        if (downloadUri != null) return

        val caption = context.getString(R.string.task_image_upload_failed)

        val intent = Intent(applicationContext, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0 /* requestCode */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        show(context) {
            contentTitle = taskBody
            contentText = caption
            notificationId = taskBody.hashCode()
            this.pendingIntent = pendingIntent
        }
    }


    companion object {
        const val UPLOADED_IMAGE_URI = "UPLOADED_IMAGE_URI"
    }
}