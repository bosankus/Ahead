package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.Todo

class TodoListAdapter(private val todoList: ArrayList<Todo>) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

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

    override fun getItemCount(): Int = todoList.size

    inner class TodoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binItems(todoList: Todo) {
            val noteBody = itemView.findViewById<TextView>(R.id.todoItemBody)
            val creationDate = itemView.findViewById<TextView>(R.id.todoItemCreationDate)
            val creationTime = itemView.findViewById<TextView>(R.id.todoItemCreationTime)

            noteBody.text = todoList.todoBody
            creationDate.text = todoList.todoCreationDate
            creationTime.text = todoList.todoCreationTime
        }
    }
}