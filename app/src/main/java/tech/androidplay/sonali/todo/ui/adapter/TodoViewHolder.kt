package tech.androidplay.sonali.todo.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.LayoutMainTodoListBinding

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:54 PM
 */

class TodoViewHolder(
    private val binding: LayoutMainTodoListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        viewModel: TaskViewModel,
        todoItem: Todo
    ) {
        binding.todo = todoItem
        binding.executePendingBindings()

        binding.clItemListContainer.setOnClickListener {
            if (todoItem.isCompleted)
                viewModel.changeTaskState(todoItem, false)
            else viewModel.changeTaskState(todoItem, true)
        }
    }
}
