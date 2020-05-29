package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import org.koin.android.ext.android.inject
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentDate

class MainActivity : AppCompatActivity() {

    // Task View Model
    private val taskViewModel by inject<TaskViewModel>()

    // Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var animation: Animation

    private val todoListAdapter: TodoListAdapter by lazy { TodoListAdapter() }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // enable white status bar with black icons
        setScreenUI()

        // turning listeners on
        clickListeners()

        // load create task button animations
        initiateFABAnimation()

        // loading all task list
        loadData()

//        loadChangedData()
    }

    override fun onStart() {
        super.onStart()
        shimmerFrameLayout.startShimmer()
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        tvTodayDate.text = getCurrentDate()
        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvTodoList.setHasFixedSize(false)
        rvTodoList.isNestedScrollingEnabled = false
    }

    private fun initiateFABAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        efabAddTask.startAnimation(animation)
    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {

        imgMenu.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            overridePendingTransition(R.anim.fade_out_animation, R.anim.fade_in_animation)
        }

        efabAddTask.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }


    private fun loadData() {
        taskViewModel.fetchTask()
        taskViewModel.fetchedTaskLiveData.observe(
            this, Observer {
                if (it != null) {
                    todoListAdapter.setListData(it)
                    showRecyclerView()
                } else {
                    frameNoTodo.visibility = View.VISIBLE
                    shimmerFrameLayout.visibility = View.GONE
                    rvTodoList.visibility = View.GONE
                }
            }
        )
    }

//    private fun loadChangedData() {
//        taskViewModel.fetchTaskStatus()
//        taskViewModel.fetchTaskStatusLiveData.observe(this, Observer {
//            if (it != null) {
//                todoListAdapter.setListItemToPosition(it, 0)
//            }
//        })
//    }

    @SuppressLint("SetTextI18n")
    private fun showRecyclerView() {

        // Recyclerview settings
        rvTodoList.isNestedScrollingEnabled = false
        rvTodoList.adapter = todoListAdapter

        // Shimmer effect untill data loads
        frameNoTodo.visibility = View.GONE
        shimmerFrameLayout.visibility = View.GONE
        rvTodoList.visibility = View.VISIBLE

        // Setting number of adapter item count
        tvTodayCount.text = todoListAdapter.itemCount.toString() + " items"
    }


}

