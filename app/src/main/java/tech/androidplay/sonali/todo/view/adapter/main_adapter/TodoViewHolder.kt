package tech.androidplay.sonali.todo.view.adapter.main_adapter

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.databinding.LayoutMainTaskListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID

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
                val bundle = bundleOf(TASK_DOC_ID to todoItem.docId)
                it?.findNavController()?.navigate(R.id.action_global_taskEditFragment, bundle)
            }
        }
    }
}
