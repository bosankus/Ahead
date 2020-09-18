package tech.androidplay.sonali.todo.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.ui.fragment.BottomSheetFragment
import tech.androidplay.sonali.todo.utils.Constants.USER_DISPLAY_IMAGE
import tech.androidplay.sonali.todo.utils.ImageHelper.selectImage
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentDate
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.UploadWorker
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var constraints: Constraints

    @Inject
    lateinit var todoAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var showFab: Animation

    private val taskViewModel: TaskViewModel by viewModels()


    @ExperimentalCoroutinesApi
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFab = AnimationUtils.loadAnimation(this, R.anim.btn_up_animation)

        // enable white status bar with black icons
        setScreenUI()

        // turning listeners on
        clickListeners()

        // load create task button animations
        initiateFABAnimation()

        loadTodo()
    }

    override fun onStart() {
        super.onStart()
        shimmerFrameLayout.startShimmer()
        val imageUrl = sharedPreferences.getString(USER_DISPLAY_IMAGE, "")
        loadUserDisplayImage(imageUrl)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    // TODO: Find work around
    private fun setScreenUI() {
        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE

        tvTodayDate.text = getCurrentDate()
        rvTodoList.apply {
            adapter = todoAdapter
        }
    }

    private fun initiateFABAnimation() {
        efabAddTask.startAnimation(showFab)
    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {

        imgUserDp.setOnClickListener { selectImage(this) }

        efabAddTask.setOnClickListener {
            val bottomSheetFragment =
                BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        rvTodoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) efabAddTask.hide()
                else if (dy < 0) efabAddTask.show()
            }
        })
    }


    @ExperimentalCoroutinesApi
    @SuppressLint("SetTextI18n")
    private fun loadTodo() {
        taskViewModel.fetchRealtime().observe(
            this,
            {
                it.let {
                    when (it) {
                        is ResultData.Loading -> {
                            shimmerFrameLayout.visibility = View.VISIBLE
                        }
                        is ResultData.Success -> {
                            logMessage("")
                            shimmerFrameLayout.visibility = View.GONE
                            todoAdapter.submitList(it.data)
                            tvTodayCount.text = todoAdapter.itemCount.toString() + " item(s)"
                        }
                        is ResultData.Failed -> {
                            shimmerFrameLayout.visibility = View.GONE
                            frameNoTodo.visibility = View.VISIBLE
                            showToast(
                                this,
                                it.toString()
                            )
                        }
                    }
                }
            }
        )
    }


    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            uploadAndShowImage(data.data!!)
        } else if (resultCode != Activity.RESULT_CANCELED)
            logMessage("Image picking cancelled")
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun uploadAndShowImage(uri: Uri?) {
        uri?.let {
            taskViewModel.uploadImage(uri).observe(this, {
                when (it) {
                    is ResultData.Loading -> logMessage("Uploading Image...")
                    is ResultData.Success -> {
                        sharedPreferences.edit().putString(USER_DISPLAY_IMAGE, it.data.toString())
                            .apply()
                        loadUserDisplayImage(it.data)
                    }
                    is ResultData.Failed -> logMessage("Something went wrong")
                }
            })
        }
    }

    private fun loadUserDisplayImage(url: String?) {
        url?.let {
            Glide.with(this)
                .load(url)
                .into(imgUserDp)
        }
    }

    // Call this method to run work manager
    /*private fun initiateUploadRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, {
                tvNoTodo.text = it.state.name
            })
    }*/
}

