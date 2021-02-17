package tech.androidplay.sonali.todo.view.adapter

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.databinding.LayoutMainTaskListBinding
import tech.androidplay.sonali.todo.utils.Constants.TASK_DATE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 8/11/2020, 4:54 PM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TodoViewHolder(
    private val binding: LayoutMainTaskListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItem: Todo) {
        binding.todo = todoItem
        binding.executePendingBindings()
        binding.tvTodoListItem.transitionName = "todoBody"

        binding.clItemListContainer.setOnClickListener {
            val bundle = bundleOf(
                TASK_DOC_ID to todoItem.docId,
                TASK_DOC_BODY to todoItem.todoBody,
                TASK_DOC_DESC to todoItem.todoDesc,
                TASK_STATUS to todoItem.isCompleted,
                TASK_DATE to todoItem.todoDate,
                TASK_IMAGE_URL to todoItem.taskImage
            )
            it?.findNavController()
                ?.navigate(R.id.action_global_taskEditFragment, bundle)
        }

    }
}
