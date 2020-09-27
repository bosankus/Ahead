package tech.androidplay.sonali.todo.ui.adapter

import android.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.LayoutMainTodoListBinding
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack

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
        todoItem: Todo,
        dialog: AlertDialog.Builder
    ) {
        binding.todo = todoItem
        binding.executePendingBindings()
        binding.tvTodoListItem.apply {
            transitionName = "todoBody"
        }

        binding.cbTaskStatus.setOnClickListener {
            if (todoItem.isCompleted)
                viewModel.updateTask(todoItem.docId, false)
            else viewModel.updateTask(todoItem.docId, true)
        }

        binding.clItemListContainer.setOnClickListener {
            val bundle = bundleOf(
                TASK_DOC_ID to todoItem.docId,
                TASK_DOC_BODY to todoItem.todoBody,
                TASK_DOC_DESC to todoItem.todoDesc,
                TASK_STATUS to todoItem.isCompleted
            )
            val extras = FragmentNavigatorExtras(
                binding.tvTodoListItem to "todoBody"
            )
            it?.findNavController()
                ?.navigate(R.id.action_taskFragment_to_taskEditFragment, bundle, null, extras)
        }

        binding.clItemListContainer.setOnLongClickListener {
            dialog.setPositiveButton("Yes") { dialogInterface, _ ->
                viewModel.deleteTask(todoItem.docId)
                showSnack(it, "you have deleted ${todoItem.todoBody}")
                dialogInterface.dismiss()
            }.create().show()
            true
        }

    }
}
