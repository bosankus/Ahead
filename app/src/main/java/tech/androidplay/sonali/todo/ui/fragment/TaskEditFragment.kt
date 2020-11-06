package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
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
import tech.androidplay.sonali.todo.utils.Constants.TASK_DATE
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_BODY
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_DESC
import tech.androidplay.sonali.todo.utils.Constants.TASK_DOC_ID
import tech.androidplay.sonali.todo.utils.Constants.TASK_IMAGE_URL
import tech.androidplay.sonali.todo.utils.Constants.TASK_STATUS
import tech.androidplay.sonali.todo.utils.Constants.TASK_TIME
import tech.androidplay.sonali.todo.utils.Constants.TIME_REQUEST_CODE
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
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

    @Inject
    lateinit var timePickerFragment: TimePickerFragment

    private val taskViewModel: TaskViewModel by viewModels()
    private var taskId: String? = ""
    private var taskBody: String? = ""
    private var taskDesc: String? = ""
    private var taskStatus: Boolean? = false
    private var taskDate: String? = ""
    private var taskTime: String? = ""
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
        taskDate = arguments?.getString(TASK_DATE) ?: "Add Reminder"
        taskTime = arguments?.getString(TASK_TIME) ?: "Add Reminder"
        taskImage = arguments?.getString(TASK_IMAGE_URL)

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
        tvSelectDate.text = taskDate
        tvSelectTime.text = taskTime
        Glide.with(requireActivity())
            .load(taskImage)
            .circleCrop()
            .into(imgTask)
            .clearOnDetach()
    }

    private fun setListener() {
        tvSelectDate.setOnClickListener { openDatePicker() }
        tvSelectTime.setOnClickListener { openTimePicker() }
        btnSaveTask.setOnClickListener { saveTask() }
        btnDeleteTask.setOnClickListener { deleteTask() }
    }

    private fun openDatePicker() {
        if (!datePickerFragment.isAdded) {
            datePickerFragment.setTargetFragment(this, Constants.DATE_REQUEST_CODE)
            datePickerFragment.show(parentFragmentManager, "DATE PICKER")
        }
    }

    private fun openTimePicker() {
        if (!timePickerFragment.isAdded) {
            timePickerFragment.setTargetFragment(this, TIME_REQUEST_CODE)
            timePickerFragment.show(parentFragmentManager, "TIME PICKER")
        }
    }

    private fun saveTask() {
        val taskBody = etTaskBody.text.toString()
        val taskDesc = etTaskDesc.text.toString()
        val todoDate = tvSelectDate.text.toString()
        val todoTime = tvSelectTime.text.toString()
        taskViewModel.updateTask(taskId, taskStatus!!, taskBody, taskDesc, todoDate, todoTime)
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            DATE_RESULT_CODE -> {
                val date = data?.getSerializableExtra(Constants.EXTRA_DATE).toString()
                pickedDate = date
                tvSelectDate.text = pickedDate
            }
            TIME_RESULT_CODE -> {
                val time = data?.getSerializableExtra(Constants.EXTRA_TIME).toString()
                pickedTime = time
                tvSelectTime.text = pickedTime
            }
        }
    }


    companion object {
        var pickedDate = ""
        var pickedTime = ""
    }
}