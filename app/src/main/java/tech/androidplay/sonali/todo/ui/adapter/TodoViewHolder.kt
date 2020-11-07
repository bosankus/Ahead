package tech.androidplay.sonali.todo.ui.adapter

import android.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.LayoutMainTodoListBinding
import tech.androidplay.sonali.todo.utils.Constants.TASK_DATE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS
import tech.androidplay.sonali.todo.utils.Constants.TASK_TIME
import tech.androidplay.sonali.todo.utils.alarmutils.cancelAlarmedNotification
import tech.androidplay.sonali.todo.utils.alarmutils.generateRequestCode

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
                viewModel.changeTaskStatus(todoItem.docId, false)
            else viewModel.changeTaskStatus(todoItem.docId, true)
        }

        binding.clItemListContainer.setOnClickListener {
            val bundle = bundleOf(
                TASK_DOC_ID to todoItem.docId,
                TASK_DOC_BODY to todoItem.todoBody,
                TASK_DOC_DESC to todoItem.todoDesc,
                TASK_STATUS to todoItem.isCompleted,
                TASK_DATE to todoItem.todoDate,
                TASK_TIME to todoItem.todoTime,
                TASK_IMAGE_URL to todoItem.taskImage
            )
            it?.findNavController()
                ?.navigate(R.id.action_taskFragment_to_taskEditFragment, bundle)
        }

        binding.clItemListContainer.setOnLongClickListener {
            dialog.setPositiveButton("Yes") { dialogInterface, _ ->
                val requestCode = todoItem.docId.generateRequestCode()
                viewModel.deleteTask(todoItem.docId)
                it.cancelAlarmedNotification(requestCode)
                dialogInterface.dismiss()
            }.create().show()
            true
        }

    }
}
