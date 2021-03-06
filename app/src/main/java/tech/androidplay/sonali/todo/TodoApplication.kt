package tech.androidplay.sonali.todo

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDex
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.utils.Constants.QUOTE_WORKER_TAG
import tech.androidplay.sonali.todo.workers.QuoteFetchWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:02 PM
 */

@HiltAndroidApp
class TodoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch { setupWork() }
    }

    private fun setupWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val quoteFetchWorker = PeriodicWorkRequestBuilder<QuoteFetchWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            QUOTE_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            quoteFetchWorker
        )
    }
}