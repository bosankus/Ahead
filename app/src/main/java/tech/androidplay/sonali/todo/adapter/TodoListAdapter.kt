package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.databinding.ActivityMainTodoListBinding

class TodoListAdapter :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun setListData(data: MutableList<Todo>) {
        todoList = data
        notifyDataSetChanged()
    }

    fun getTodoForPosition(position: Int): Todo {
        return todoList[position]
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoListAdapter.TodoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ActivityMainTodoListBinding =
            DataBindingUtil.inflate(inflater, R.layout.activity_main_todo_list, parent, false)
        return TodoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoListAdapter.TodoListViewHolder, position: Int) {

        holder.binItems(todoList[position])
    }

    override fun getItemCount(): Int {
        return if (todoList.size > 0) {
            todoList.size
        } else 0
    }

    inner class TodoListViewHolder(val binding: ActivityMainTodoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binItems(todoList: Todo) {
            binding.todo = todoList
            binding.executePendingBindings()
        }

    }
}

