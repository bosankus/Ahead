package tech.androidplay.sonali.todo.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fxn.BubbleTabBar
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.Todo
import tech.androidplay.sonali.todo.utils.Helper
import tech.androidplay.sonali.todo.utils.TimeStampUtil

class MainActivity : AppCompatActivity() {

    private val currentDate: String by lazy { TimeStampUtil().currentDate }
    private val currentTime: String by lazy { TimeStampUtil().currentTime }
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }


        firebaseAuth = FirebaseAuth.getInstance()

        // load animations
        val btnAnimation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        btnAddTodo.startAnimation(btnAnimation)

        showTodoList()

        Helper().showToast(this, firebaseAuth.currentUser?.email.toString())

    }



    // TodoList recyclerview
    private fun showTodoList() {
        todoListRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val todo = ArrayList<Todo>()
        todo.add(Todo("1", "Get update from you broadband.", currentDate, currentTime))
        todo.add(Todo("2", "Get update from you Office", currentDate, currentTime))
        val adapter = TodoListAdapter(todo)
        todoListRecyclerview.adapter = adapter
    }
}

