package tech.androidplay.sonali.todo.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 22/Aug/2020
 * Email: ankush@androidplay.in
 */

// TODO: Create work manager to call firestore API at a fixed interval and shows notification with loading loader

class UploadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        return try {
            for (i in 1..600000) {
                logMessage("Uploading $i")
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}