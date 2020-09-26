package tech.androidplay.sonali.todo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_edit.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS
import tech.androidplay.sonali.todo.utils.PermissionManager.askStoragePermission
import tech.androidplay.sonali.todo.utils.UIHelper.selectImage
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    private val taskViewModel: TaskViewModel by viewModels()
    private var taskId: String? = ""
    private var taskBody: String? = ""
    private var taskDesc: String? = ""
    private var taskStatus: Boolean? = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpScreen()
        setListener()
    }

    private fun setUpScreen() {
        taskId = arguments?.getString(TASK_DOC_ID)
        taskBody = arguments?.getString(TASK_DOC_BODY)
        taskDesc = arguments?.getString(TASK_DOC_DESC)
        taskStatus = arguments?.getBoolean(TASK_STATUS)

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
    }

    private fun setListener() {
        imgDeleteTask.setOnClickListener {
            taskViewModel.deleteTask(taskId)
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
            showSnack(requireView(), "Task Deleted")
        }

        imgAttachFile.setOnClickListener {
            askStoragePermission(this)
            requireActivity().selectImage()
        }

        efabSaveTask.setOnClickListener {
            val taskBody = etTaskBody.text.toString()
            val taskDesc = etTaskBody.text.toString()
            taskViewModel.updateTask(taskId, taskStatus!!, taskBody, taskDesc)
            showSnack(requireView(), "Task Deleted")
        }
    }

}