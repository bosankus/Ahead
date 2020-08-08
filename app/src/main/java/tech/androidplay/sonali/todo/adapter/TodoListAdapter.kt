package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.ActivityMainTodoListBinding
import tech.androidplay.sonali.todo.databinding.setAlpha
import tech.androidplay.sonali.todo.utils.UIHelper.removeStrikeText
import tech.androidplay.sonali.todo.utils.UIHelper.strikeText

class TodoListAdapter(private val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {

    private var todoList = mutableListOf<Todo>()

    fun setListData(data: MutableList<Todo>) {
        todoList = data
        notifyDataSetChanged()
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
                if (binding.rbTodoItemStatus.isChecked) {
                    completeTask(todoList.docId, false)
                    binding.rbTodoItemStatus.isChecked = false
                    removeStrikeText(binding.tvTodoListItem)
                    setAlpha(binding.clItemListContainer, false)
                } else if (!binding.rbTodoItemStatus.isChecked) {
                    completeTask(todoList.docId, true)
                    binding.rbTodoItemStatus.isChecked = true
                    strikeText(binding.tvTodoListItem)
                    setAlpha(binding.clItemListContainer, true)
                }
            }
            binding.executePendingBindings()
        }
    }

    fun completeTask(docId: String, status: Boolean) {
        viewModel.completeTask(docId, status)
    }
}

