package tech.androidplay.sonali.todo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cache: CacheManager

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }

    private fun navigateToGlobalFragment(intent: Intent) {
        if (intent.action == ACTION_SHOW_TASK_FRAGMENT)
            navHostFragment.findNavController().navigate(R.id.action_global_taskFragment)
    }
}

