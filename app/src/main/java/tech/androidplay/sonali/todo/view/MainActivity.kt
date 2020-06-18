package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import org.koin.android.ext.android.inject
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.network.ImageManager.parseData
import tech.androidplay.sonali.todo.network.ImageManager.selectImage
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentDate
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

class MainActivity : AppCompatActivity() {

    // Task View Model
    private val taskViewModel by inject<TaskViewModel>()
    private val todoListAdapter by inject<TodoListAdapter>()

    private lateinit var showFab: Animation

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

//        loadChangedData()
    }

    override fun onStart() {
        super.onStart()
        shimmerFrameLayout.startShimmer()
        // loading all task list
        loadData()
    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }


    private fun setScreenUI() {
        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE

        tvTodayDate.text = getCurrentDate()
        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTodoList.setHasFixedSize(false)
    }

    private fun initiateFABAnimation() {
        efabAddTask.startAnimation(showFab)
    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {

        /*imgMenu.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            overridePendingTransition(R.anim.fade_out_animation, R.anim.fade_in_animation)
        }*/

        imgMenu.setOnClickListener { selectImage(this) }

        efabAddTask.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        rvTodoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) efabAddTask.hide()
                else if (dy < 0) efabAddTask.show()
            }
        })

    }


    private fun loadData() {
        taskViewModel.fetchTask()
        logMessage("hey")
        try {
            taskViewModel.fetchedTaskLiveData.observe(
                this, Observer {
                    if (it == null) {
                        shimmerFrameLayout.visibility = View.GONE
                        rvTodoList.visibility = View.GONE
                        frameNoTodo.visibility = View.VISIBLE
                    } else {
                        todoListAdapter.setListData(it)
                        showRecyclerView()
                    }
                }
            )
        } catch (e: Exception) {
            logMessage("$e")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showRecyclerView() {

        // Recyclerview settings
        rvTodoList.smoothScrollToPosition(0)
        rvTodoList.adapter = todoListAdapter

        // Shimmer effect until data loads
        frameNoTodo.visibility = View.GONE
        shimmerFrameLayout.visibility = View.GONE
        rvTodoList.visibility = View.VISIBLE

        // Setting number of adapter item count
        tvTodayCount.text = todoListAdapter.itemCount.toString() + " items"
    }


    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            taskViewModel.uploadImage("32520", "DLback", parseData(data))//36294
        } else if (resultCode != Activity.RESULT_CANCELED)
            logMessage("Image picking cancelled")
        super.onActivityResult(requestCode, resultCode, data)
    }
}

