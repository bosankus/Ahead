package tech.androidplay.sonali.todo.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.fxn.OnBubbleClickListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.Todo
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

        // Setting default fragment
        supportFragmentManager.inTransaction {
            replace(R.id.container, HomeFragment())
        }

        // load bottom navigation bar animations
        val animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        mainBottomBar.startAnimation(animation)

        // turning listeners on
        clickListeners()

        // showing list of todo items
        showTodoList()

    }

    private fun clickListeners() {
        mainBottomBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                when (id) {
                    R.id.main -> supportFragmentManager.inTransaction {
                        replace(R.id.container, HomeFragment())
                    }
                    R.id.alarm -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        replace(R.id.container, AlarmFragment())
                    }
                    R.id.event -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        add(R.id.container, EventFragment())
                    }
                    R.id.profile -> supportFragmentManager.inTransaction {
                        this.addToBackStack(null)
                        replace(R.id.container, ProfileFragment())
                    }
                }
            }
        })
    }

    // Fragment Manager Transaction function
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func().commit()
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

