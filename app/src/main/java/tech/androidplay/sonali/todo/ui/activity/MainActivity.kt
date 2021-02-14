package tech.androidplay.sonali.todo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.AuthManager
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.DEVICE_ANDROID_VERSION
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cache: CacheManager
    private lateinit var authManager: AuthManager

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        authManager = AuthManager(this)
        navigateToGlobalFragment(intent)
        setScreenUI()
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
    @SuppressLint("ResourceAsColor")
    private fun setScreenUI() {
        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE

        // To support portrait view in API 26
        if (DEVICE_ANDROID_VERSION != ANDROID_OREO) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        authManager.handleAuth(requestCode, resultCode, data) { isSuccessful, error ->
            if (isSuccessful) {
                navHostFragment.findNavController().navigate(R.id.action_global_taskFragment)
            } else showSnack(
                findViewById(R.id.activityMain), getString(
                    R.string.prompt_failed_to_login,
                    error?.localizedMessage ?: "Unknown Error"
                )
            )
        }
    }

    private fun navigateToGlobalFragment(intent: Intent) {
        if (intent.action == ACTION_SHOW_TASK_FRAGMENT)
            navHostFragment.findNavController().navigate(R.id.action_global_taskFragment)
    }
}

