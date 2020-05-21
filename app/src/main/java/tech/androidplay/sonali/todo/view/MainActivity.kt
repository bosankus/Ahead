package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_todo_list.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.Helper.getCurrentDate
import tech.androidplay.sonali.todo.utils.Helper.logMessage

class MainActivity : AppCompatActivity() {

    // Task View Model
    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)
            .get(TaskViewModel::class.java)
    }

    // Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUserId = firebaseAuth.currentUser?.uid.toString()

    private lateinit var animation: Animation

    private val position: Int = 0
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

    private fun setScreenUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        tvTodayDate.text = getCurrentDate()
        rvTodoList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        rvTodoList.setHasFixedSize(true)
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
            val arguments = Bundle()
            arguments.putString("userId", firebaseUserId)
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.arguments = arguments
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        // TODO: Not working
        rbTodoItemStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tvTodoListItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvTodoListItemDesc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            if (!isChecked) {
                tvTodoListItem.paintFlags = 0
                tvTodoListItemDesc.paintFlags = 0
            }
        }
    }


    private fun loadData() {
        taskViewModel.fetchTask()
        taskViewModel.fetchedTaskLiveData.reObserver(
            this, Observer {
                todoListAdapter.setListData(it)
                showRecyclerView()
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showRecyclerView() {

        // Recyclerview settings
        rvTodoList.adapter = todoListAdapter
        todoListAdapter.notifyDataSetChanged()

        // Shimmer effect untill data loads
        frameNoTodo.visibility = View.GONE
        shimmerFrameLayout.visibility = View.GONE
        rvTodoList.visibility = View.VISIBLE

        // Setting number of adapter item count
        tvTodayCount.text = todoListAdapter.itemCount.toString() + " items"
    }


    private fun <T> MutableLiveData<T>.reObserver(owner: LifecycleOwner, observer: Observer<T>) {
        removeObserver(observer)
        logMessage("Observer Removed")
        observe(owner, observer)
        logMessage("Observer is observing again")
    }

}

