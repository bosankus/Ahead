package tech.androidplay.sonali.todo.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.GLOBAL_TAG

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 22/Aug/2020
 * Email: ankush@androidplay.in
 */
class UploadWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        try {

            // Getting the input
            val inputData = inputData.getString(GLOBAL_TAG)

            // Getting response from upload method
            val response = uploadTodo(inputData)

            val someString = response.toString()

            val context = workDataOf(GLOBAL_TAG to someString)

        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }

    private fun uploadTodo(inputString: String?): Result {
        TODO("Not yet implemented")
    }
}