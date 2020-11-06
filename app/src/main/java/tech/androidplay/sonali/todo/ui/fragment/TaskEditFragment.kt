package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Extensions.loadImageCircleCropped
import tech.androidplay.sonali.todo.utils.Extensions.openDatePicker
import tech.androidplay.sonali.todo.utils.Extensions.openTimePicker
import tech.androidplay.sonali.todo.utils.Extensions.selectImage
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.cancelAlarmedNotification
import tech.androidplay.sonali.todo.utils.alarmutils.generateRequestCode
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
import java.util.*
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

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var calendar: Calendar

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
        taskImage?.let { imgTask.loadImageCircleCropped(it) }
    }

    private fun setListener() {
        tvSelectDate.setOnClickListener { openDatePicker(datePickerFragment) }
        tvSelectTime.setOnClickListener { openTimePicker(timePickerFragment) }
        btnSaveTask.setOnClickListener { saveTask() }
        btnDeleteTask.setOnClickListener { deleteTask() }
        imgTask.setOnClickListener { selectImage(this) }
    }


    private fun saveTask() {
        val taskBody = etTaskBody.text.toString()
        val taskDesc = etTaskDesc.text.toString()
        val todoDate = tvSelectDate.text.toString()
        val todoTime = tvSelectTime.text.toString()
        taskViewModel.updateTask(taskId, taskStatus!!, taskBody, taskDesc, todoDate, todoTime)
        showSnack(requireView(), "Task Saved")
    }


    private fun changeImage(pickedImage: Uri?) {
        taskViewModel.uploadImage(pickedImage, taskId!!).observe(viewLifecycleOwner, { imageUrl ->
            when (imageUrl) {
                is ResultData.Loading -> showToast(requireContext(), "Uploading Image")
                is ResultData.Success -> {
                    val url = imageUrl.data
                    url?.let { imgTask.loadImageCircleCropped(it) }
                }
                is ResultData.Failed -> showToast(requireContext(), "Something went wrong")
            }
        })
    }


    private fun deleteTask() {
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            taskViewModel.deleteTask(taskId)
            val requestCode = taskId!!.generateRequestCode()
            cancelAlarmedNotification(requestCode)
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
            Activity.RESULT_OK -> {
                pickedImage = data?.data
                changeImage(pickedImage)
            }
        }
    }

    companion object {
        var pickedDate = ""
        var pickedTime = ""
        var pickedImage: Uri? = null
    }
}