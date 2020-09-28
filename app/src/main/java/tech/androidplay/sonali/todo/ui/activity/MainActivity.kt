package tech.androidplay.sonali.todo.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var constraints: Constraints

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setScreenUI()
    }


    // TODO: Find work around
    @SuppressLint("ResourceAsColor")
    private fun setScreenUI() {
        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
    }


    // Call this method to run work manager
    /*private fun initiateUploadRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, {
                logMessage(it.state.name)
            })
    }*/
}

