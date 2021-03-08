package tech.androidplay.sonali.todo.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.ActivityMainBinding
import tech.androidplay.sonali.todo.utils.AuthManager
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import tech.androidplay.sonali.todo.utils.Constants.ANDROID_OREO
import tech.androidplay.sonali.todo.utils.Constants.DEVICE_ANDROID_VERSION
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var cache: CacheManager
    private lateinit var authManager: AuthManager
    private val viewModel: AuthViewModel by viewModels()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    private fun navigateToGlobalFragment(intent: Intent) {
        if (intent.action == ACTION_SHOW_TASK_FRAGMENT)
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_taskFragment)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            authManager.handleAuth(requestCode, resultCode, data) { isSuccessful, error ->
                if (isSuccessful) {
                    val userDetails = authManager.userDetails
                    userDetails?.let { viewModel.saveUserData(it) }
                    findNavController(R.id.navHostFragment).navigate(R.id.action_global_taskFragment)
                } else showSnack(
                    findViewById(R.id.activityMain), getString(
                        R.string.prompt_failed_to_login,
                        error?.localizedMessage ?: "Unknown Error"
                    )
                )
            }
        }
    }

}

