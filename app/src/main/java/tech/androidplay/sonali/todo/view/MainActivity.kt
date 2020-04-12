package tech.androidplay.sonali.todo.view

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import kotlinx.android.synthetic.main.activity_main.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.model.TodoList
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val calendarDate: Calendar = Calendar.getInstance()
    private var currentMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // enable white status bar with black icons
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }


        // load animations
        val btnAnimation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        btnAddTodo.startAnimation(btnAnimation)

        showTodoList()

        // set current date month
        calendarDate.time = Date()
        currentMonth = calendarDate[Calendar.MONTH]

        // Setting up Calender UI
        val calendarViewManager = object : CalendarViewManager {
            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                TODO("Not yet implemented")
            }

            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                TODO("Not yet implemented")
            }
        }

        val selectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    // TodoList recyclerview
    private fun showTodoList() {
        todoListRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val todo = ArrayList<TodoList>()
        todo.add(TodoList("Get update from you broadband.", "Today", "11:09"))
        todo.add(TodoList("Got to MORE for groceries", "Today", "10:00"))
        todo.add(TodoList("Call Air India BLR Airport branch", "Yesterday", "21:00"))
        val adapter = TodoListAdapter(todo)
        todoListRecyclerview.adapter = adapter
    }

    private fun strikeText(text: TextView) {

    }
}

