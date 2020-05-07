package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frame_today_todo_header.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.TimeStampUtil

class MainActivity : AppCompatActivity() {

    private val currentDate: String by lazy { TimeStampUtil().currentDate }
    private val currentTime: String by lazy { TimeStampUtil().currentTime }
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initiating firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // enable white status bar with black icons
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.WHITE

        // load bottom navigation bar animations
//        val animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
//        floatingButton.startAnimation(animation)
        tvTodayDate.text = currentDate

        // loading all task list
        loadToDoList()

        // turning listeners on
        clickListeners()

    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {
        efabAddTask.setOnClickListener {
            val arguments = Bundle()
            arguments.putString("userId", firebaseAuth.currentUser?.uid.toString())
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.arguments = arguments
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


    }

    private fun loadToDoList() {
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        taskViewModel.readTaskFromFirestore()
    }

}

