package tech.androidplay.sonali.todo

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDex
import androidx.work.*
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.utils.Constants.QUOTE_WORKER_TAG
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
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
    private val devKey = "aUeLMSf4msnzSfiTVSyZvC"

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()

        // set up work manager
        delayedInit()

        // setup AppsFlyer tracking
        initiateTracking()
    }

    private fun initiateTracking() {
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map { logMessage("conversion_attribute:  ${it.key} = ${it.value}") }
                }
            }

            override fun onConversionDataFail(p0: String?) {
                logMessage("error onAttributionFailure: $p0")
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                p0?.let { logMessage("onAppOpen_attribute: $it") }
            }

            override fun onAttributionFailure(p0: String?) {
                logMessage("error onAttributionFailure: $p0")
            }
        }
        AppsFlyerLib.getInstance().init(devKey, conversionDataListener, this)
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