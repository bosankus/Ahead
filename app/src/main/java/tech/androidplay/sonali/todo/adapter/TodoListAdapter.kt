package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.ActivityMainTodoListBinding

class TodoListAdapter(private val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun setListData(data: MutableList<Todo>) {
        todoList = data
        notifyItemInserted(0)
//        notifyDataSetChanged()
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
        holder.bindItems(todoList[position])
    }

    override fun getItemCount(): Int {
        return if (todoList.size > 0) {
            todoList.size
        } else 0
    }

    inner class TodoListViewHolder(private val binding: ActivityMainTodoListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(todoList: Todo) {
            binding.todo = todoList
            binding.clItemListContainer.setOnClickListener {
                if (todoList.isCompleted)
                    completeTask(todoList.docId, false)
                else completeTask(todoList.docId, true)
            }
            binding.executePendingBindings()
        }
    }

    fun completeTask(docId: String, status: Boolean) {
        viewModel.completeTask(docId, status)
    }
}

