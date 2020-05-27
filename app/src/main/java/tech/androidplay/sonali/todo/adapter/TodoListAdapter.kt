package tech.androidplay.sonali.todo.adapter

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo

class TodoListAdapter :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun setListData(data: MutableList<Todo>) {
        todoList = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoListAdapter.TodoListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_todo_list, parent, false)
        return TodoListViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoListAdapter.TodoListViewHolder, position: Int) {
        holder.binItems(todoList[position])
    }

    override fun getItemCount(): Int {
        return if (todoList.size > 0) {
            todoList.size
        } else 0
    }

    inner class TodoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binItems(todoList: Todo) {
            // initializing view elements
            val clItemListContainer =
                itemView.findViewById<ConstraintLayout>(R.id.clItemListContainer)
            val rbTodoItemStatus = itemView.findViewById<RadioButton>(R.id.rbTodoItemStatus)
            val tvTodoListItem = itemView.findViewById<TextView>(R.id.tvTodoListItem)
            val tvTodoListItemDesc = itemView.findViewById<TextView>(R.id.tvTodoListItemDesc)

            // setting views
            tvTodoListItem.text = todoList.todoBody
            tvTodoListItemDesc.text = todoList.todoDesc

            // Onclick item
            clItemListContainer.setOnClickListener {
                if (rbTodoItemStatus.isChecked) {
                    rbTodoItemStatus.isChecked = false
                    removeStrikeText(tvTodoListItem)
                    removeStrikeText(tvTodoListItemDesc)
                } else if (!rbTodoItemStatus.isChecked) {
                    rbTodoItemStatus.isChecked = true
                    strikeText(tvTodoListItem)
                    strikeText(tvTodoListItemDesc)
                }
            }
        }
    }

    fun strikeText(textView: TextView) {
        val text = textView.text.toString()
        val spannable = SpannableString(text)
        spannable.setSpan(StrikethroughSpan(), 0, text.length, 0)
        textView.text = spannable
    }

    fun removeStrikeText(textView: TextView) {
        val text = textView.text.toString()
        val spannable = SpannableString(text)
        spannable.removeSpan(StrikethroughSpan())
        textView.text = spannable
    }
}
