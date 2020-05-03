package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.TimeStampUtil

class MainActivity : AppCompatActivity() {

    private val currentDate: String by lazy { TimeStampUtil().currentDate }
    private val currentTime: String by lazy { TimeStampUtil().currentTime }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initiating firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance()

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        // load bottom navigation bar animations
//        val animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
//        floatingButton.startAnimation(animation)

        // loading all task list
        loadToDoList()

        // turning listeners on
        clickListeners()

    }

    @SuppressLint("InflateParams")
    private fun clickListeners() {
        efabAddTask.setOnClickListener {
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }


    }

    private fun loadToDoList() {
        rvTodoList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val todo = ArrayList<Todo>()
        todo.add(
            Todo(
                "1",
                "Get update from you broadband.",
                "Design stuff",
                false,
                null,
                null
            )
        )
        todo.add(
            Todo(
                "1",
                "UI found from Dribbble",
                "Self stuff",
                true,
                null,
                null
            )
        )
        val adapter = TodoListAdapter(todo)
        rvTodoList.adapter = adapter
    }
}

