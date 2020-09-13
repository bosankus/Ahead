package tech.androidplay.sonali.todo.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.ui.fragment.BottomSheetFragment
import tech.androidplay.sonali.todo.utils.ImageHelper.selectImage
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentDate
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
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

    private lateinit var showFab: Animation

    private val taskViewModel: TaskViewModel by viewModels()

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

        load()
    }

    override fun onStart() {
        super.onStart()
        shimmerFrameLayout.startShimmer()
        // loading all task list
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


    @SuppressLint("SetTextI18n")
    private fun load() {
        taskViewModel.fetchedTaskLiveData.observe(this, {
            if (it.isNotEmpty()) {
                shimmerFrameLayout.visibility = View.GONE
                todoAdapter.submitList(it.toMutableList())
                tvTodayCount.text = todoAdapter.itemCount.toString() + " item(s)"
                frameNoTodo.visibility = View.VISIBLE // just to make screen look good
            } else {
                shimmerFrameLayout.visibility = View.GONE
                frameNoTodo.visibility = View.VISIBLE
            }
        })
    }


    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            taskViewModel.uploadImage(data.data!!)
        } else if (resultCode != Activity.RESULT_CANCELED)
            logMessage("Image picking cancelled")
        super.onActivityResult(requestCode, resultCode, data)
    }


    // Call this method to run work manager
    private fun initiateUploadRequest() {
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadRequest)
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, {
                tvNoTodo.text = it.state.name
            })
    }
}
