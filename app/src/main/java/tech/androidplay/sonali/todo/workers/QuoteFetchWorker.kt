package tech.androidplay.sonali.todo.workers

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.androidplay.sonali.todo.data.repository.QuoteRepository
import tech.androidplay.sonali.todo.utils.Constants.QUOTE
import tech.androidplay.sonali.todo.utils.Constants.QUOTE_AUTHOR
import tech.androidplay.sonali.todo.utils.Constants.QUOTE_WORKER_ID
import tech.androidplay.sonali.todo.utils.Notify
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/March/2021
 * Email: ankush@androidplay.in
 */

/**
 * This helps to make one network calls each day to get daily qoute and saves in shared preferences.
 */

@HiltWorker
class QuoteFetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val preferences: SharedPreferences,
    private val repository: QuoteRepository
) :
    CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var notify: Notify
    private val context = applicationContext

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        showNotification()
        val response = repository.fetchQuote()
        if (response.text?.isNotEmpty() == true) {
            preferences.edit {
                putString(QUOTE, response.text)
                putString(QUOTE_AUTHOR, "${response.author}")
                apply()
            }
            Result.success()
        } else {
            showErrorNotification()
            Result.failure()
        }
        Result.failure()
    }

    private fun showNotification() {
        notify.showNotification(context) {
            notificationId = QUOTE_WORKER_ID
            notificationTitle = "Syncing..."
            notificationBody = "Managing your tasks on your behalf"
        }
    }

    private fun showErrorNotification() {
        notify.showNotification(context) {
            notificationId = QUOTE_WORKER_ID
            notificationTitle = "Syncing Failed"
            notificationBody = "Ehh! something stopped the process."
        }
    }
}