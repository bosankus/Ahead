package tech.androidplay.sonali.todo.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_edit.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.picker.DatePickerFragment
import tech.androidplay.sonali.todo.ui.picker.TimePickerFragment
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.Constants.DATE_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.Constants.TASK_REMINDER
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS
import tech.androidplay.sonali.todo.utils.Constants.TIME_REQUEST_CODE
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Sep/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var datePickerFragment: DatePickerFragment

    /*@Inject
    lateinit var timePickerFragment: TimePickerFragment*/

    private val taskViewModel: TaskViewModel by viewModels()
    private var taskId: String? = ""
    private var taskBody: String? = ""
    private var taskDesc: String? = ""
    private var taskStatus: Boolean? = false
    private var taskReminder: String? = ""
    private var taskImage: String? = ""

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
        taskReminder = arguments?.getString(TASK_REMINDER) ?: "Add Reminder"
        taskImage = arguments?.getString(TASK_IMAGE_URL)

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
        chipTaskAlarmTime.text = taskReminder
        Glide.with(requireActivity())
            .load(taskImage)
            .circleCrop()
            .into(imgTask)
            .clearOnDetach()
    }

    private fun setListener() {
        chipTaskAlarmTime.setOnClickListener { openDateTimePicker() }
        efabSaveTask.setOnClickListener { saveTask() }
        imgDeleteTask.setOnClickListener { deleteTask() }
    }

    private fun openDateTimePicker() {
        if (!datePickerFragment.isAdded) {
            datePickerFragment.setTargetFragment(this, Constants.DATE_REQUEST_CODE)
            datePickerFragment.show(parentFragmentManager, "DATE PICKER")
        }
    }

    private fun saveTask() {
        val taskBody = etTaskBody.text.toString()
        val taskDesc = etTaskDesc.text.toString()
        taskViewModel.updateTask(taskId, taskStatus!!, taskBody, taskDesc)
        showSnack(requireView(), "Task Saved")
    }

    private fun deleteTask() {
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            taskViewModel.deleteTask(taskId)
            showSnack(requireView(), "Task Deleted")
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
            dialogInterface.dismiss()
        }.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            DATE_RESULT_CODE -> {
                val date = data?.getSerializableExtra(Constants.EXTRA_DATE).toString()
                pickedDate = date
            }
            TIME_REQUEST_CODE -> {
                val time = data?.getSerializableExtra(Constants.EXTRA_TIME).toString()
                pickedTime = time
            }
        }
    }

    companion object {
        var pickedDate = ""
        var pickedTime = ""
    }
}