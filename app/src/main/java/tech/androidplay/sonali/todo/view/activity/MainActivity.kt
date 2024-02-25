package tech.androidplay.sonali.todo.view.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.ActivityMainBinding
import tech.androidplay.sonali.todo.receiver.AlarmReceiver
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.DEVICE_ANDROID_VERSION
import tech.androidplay.sonali.todo.utils.startInAppUpdate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appsFlyerLib: AppsFlyerLib
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var cache: CacheManager

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appsFlyerLib.start(this)

        startInAppUpdate(this)

        managePendingAlarm()

        navigateToGlobalFragment(intent)
        setScreenUI()
    }


    override fun onResume() {
        super.onResume()
        startInAppUpdate(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            navigateToGlobalFragment(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cache.clearCache(this)
    }


    // TODO: Find work around
    @SuppressLint("ResourceAsColor", "SourceLockedOrientationActivity")
    private fun setScreenUI() {
        // To support portrait view in API 26
        if (DEVICE_ANDROID_VERSION != ANDROID_OREO) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    // Enables the AlarmReceiver
    private fun managePendingAlarm() {
        val receiver = ComponentName(applicationContext, AlarmReceiver::class.java)

        applicationContext.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }


    private fun navigateToGlobalFragment(intent: Intent) {
        if (intent.action == ACTION_SHOW_TASK_FRAGMENT)
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_taskFragment)
    }
}
