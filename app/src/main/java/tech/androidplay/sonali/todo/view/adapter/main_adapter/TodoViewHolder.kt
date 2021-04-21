package tech.androidplay.sonali.todo.view.adapter.main_adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.databinding.LayoutMainTaskListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.view.fragment.TaskFragmentDirections

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:54 PM
 */

class TodoViewHolder(
    private val binding: LayoutMainTaskListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.apply {
            todo = todoItem
            executePendingBindings()
            clItemListContainer.setOnClickListener {
                val action =
                    TaskFragmentDirections.actionTaskFragmentToTaskEditFragment(todoItem.docId)
                it?.findNavController()?.navigate(action)
            }
        }
    }
}
