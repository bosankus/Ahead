package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            val tvTodoListItem = itemView.findViewById<TextView>(R.id.tvTodoListItem)
            val tvTodoListItemDesc = itemView.findViewById<TextView>(R.id.tvTodoListItemDesc)

            // setting views
            val todoId: String = todoList.todoId
            tvTodoListItem.text = todoList.todoBody
            tvTodoListItemDesc.text = todoList.todoDesc
        }
    }
}
