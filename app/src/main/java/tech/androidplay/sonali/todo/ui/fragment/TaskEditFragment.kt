package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_edit.*
import kotlinx.android.synthetic.main.layout_date_time.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
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
import tech.androidplay.sonali.todo.utils.Constants.TASK_TIME
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Extensions.compareWith
import tech.androidplay.sonali.todo.utils.Extensions.loadImageCircleCropped
import tech.androidplay.sonali.todo.utils.Extensions.openDatePicker
import tech.androidplay.sonali.todo.utils.Extensions.openTimePicker
import tech.androidplay.sonali.todo.utils.Extensions.selectImage
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
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

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@SuppressLint("SetTextI18n")
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

    @Inject
    lateinit var pendingIntent: PendingIntent

    private val taskViewModel: TaskViewModel by viewModels()

    //    private var alarmId: String = ""
    private var taskId: String? = ""
    private var taskBody: String = ""
    private var taskDesc: String = ""
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
        logMessage(taskId!!)
        taskBody = arguments?.getString(TASK_DOC_BODY)!!
        taskDesc = arguments?.getString(TASK_DOC_DESC)!!
        taskDate = arguments?.getString(TASK_DATE) ?: "Add Reminder"
        taskTime = arguments?.getString(TASK_TIME) ?: "Add Reminder"
        taskImage = arguments?.getString(TASK_IMAGE_URL)

        etTaskBody.setText(taskBody)
        etTaskDesc.setText(taskDesc)
        tvSelectDate.text = taskDate
        tvSelectTime.text = taskTime
        if (!taskImage.isNullOrEmpty()) {
            imgTask.loadImageCircleCropped(taskImage!!)
            imgTaskNoImage.visibility = View.INVISIBLE
            imgTask.visibility = View.VISIBLE
        } else {
            imgTaskNoImage.visibility = View.VISIBLE
            imgTask.visibility = View.INVISIBLE
        }
    }

    private fun setListener() {
        layoutEditDateTime.setOnClickListener { openDatePicker(datePickerFragment) }
        btnSaveTask.setOnClickListener { saveTask() }
        btnDeleteTask.setOnClickListener { deleteTask(taskId) }
        imgTask.setOnClickListener { selectImage(this) }
        imgTaskNoImage.setOnClickListener { selectImage(this) }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            saveTask()
            showSnack(requireView(), "Task Saved")
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
        }
    }


    private fun saveTask() {
        val taskBody = etTaskBody.text.toString()
        val taskDesc = etTaskDesc.text.toString()
        val taskDate = tvSelectDate.text.toString()
        val taskTime = tvSelectTime.text.toString()
        if (!this.taskBody.compareWith(taskBody) ||
            !this.taskDesc.compareWith(taskDesc)
        ) {
            taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate, taskTime)
        }
        if (!this.taskDate?.compareWith(taskDate)!! ||
            !this.taskTime?.compareWith(taskTime)!!
        ) {
            taskViewModel.updateTask(taskId!!, taskBody, taskDesc, taskDate, taskTime)
            val requestCode = taskId!!.generateRequestCode()
            startAlarmedNotification(requestCode, taskBody, taskDesc, calendar, alarmManager)
        }
    }


    private fun changeImage(pickedImage: Uri?) {
        taskViewModel.uploadImage(pickedImage, taskId!!).observe(viewLifecycleOwner, { imageUrl ->
            when (imageUrl) {
                is ResultData.Loading -> showToast(requireContext(), "Uploading Image")
                is ResultData.Success -> {
                    val url = imageUrl.data
                    url?.let { imgTask.loadImageCircleCropped(it) }
                    imgTaskNoImage.visibility = View.INVISIBLE
                    imgTask.visibility = View.VISIBLE
                }
                is ResultData.Failed -> showToast(requireContext(), "Something went wrong")
            }
        })
    }

    private fun deleteTask(docId: String?) {
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            val requestCode = docId!!.generateRequestCode()
            taskViewModel.deleteTask(docId)
            cancelAlarmedNotification(requestCode)
            findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
            dialogInterface.dismiss()
            showToast(requireContext(), "Task deleted.")
        }.create().show()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            DATE_RESULT_CODE -> {
                val date = data?.getSerializableExtra(Constants.EXTRA_DATE).toString()
                pickedDate = date
                tvSelectDate.text = pickedDate
                openTimePicker(timePickerFragment)
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