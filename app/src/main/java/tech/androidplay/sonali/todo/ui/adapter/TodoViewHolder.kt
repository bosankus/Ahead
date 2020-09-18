package tech.androidplay.sonali.todo.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.databinding.LayoutMainTodoListBinding
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:54 PM
 */
class TodoViewHolder(
    private val binding: LayoutMainTodoListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()
    }
}