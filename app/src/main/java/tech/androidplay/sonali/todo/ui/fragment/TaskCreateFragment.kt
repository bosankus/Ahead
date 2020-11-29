package tech.androidplay.sonali.todo.ui.fragment

import android.app.Activity
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_task_create.*
import kotlinx.android.synthetic.main.layout_date_time.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.picker.DatePickerFragment
import tech.androidplay.sonali.todo.ui.picker.TimePickerFragment
import tech.androidplay.sonali.todo.utils.Constants.DATE_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_DATE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_TIME
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Extensions.hideKeyboard
import tech.androidplay.sonali.todo.utils.Extensions.openDatePicker
import tech.androidplay.sonali.todo.utils.Extensions.openTimePicker
import tech.androidplay.sonali.todo.utils.Extensions.selectImage
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import tech.androidplay.sonali.todo.utils.alarmutils.generateRequestCode
import tech.androidplay.sonali.todo.utils.alarmutils.startAlarmedNotification
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class TaskCreateFragment : Fragment(R.layout.fragment_task_create) {

    @Inject
    lateinit var datePickerFragment: DatePickerFragment

    @Inject
    lateinit var timePickerFragment: TimePickerFragment

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var calendar: Calendar

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        clickListeners()
    }

    private fun clickListeners() {
        layoutDateTime.setOnClickListener { openDatePicker(datePickerFragment) }
        tvSelectImage.setOnClickListener { selectImage(this) }
        btCreateTask.setOnClickListener {
            requireActivity().hideKeyboard()
            if ((tvTaskInput.text.length) <= 0) tvTaskInput.error = "Cant't be empty!"
            else createTask()
        }
    }


    private fun createTask() {
        val todoBody = tvTaskInput.text.toString()
        val todoDesc = tvTaskDescInput.text.toString()
        val todoDate = tvSelectDate.text.toString()
        val todoTime = tvSelectTime.text.toString()

        taskViewModel.createTask(todoBody, todoDesc, todoDate, todoTime, taskImage)
            .observe(viewLifecycleOwner, {
                it?.let {
                    when (it) {
                        is ResultData.Loading -> handleLoading()
                        is ResultData.Success -> handleSuccess(it.data!!, todoBody, todoDesc)
                        is ResultData.Failed -> handleFailure()
                    }
                }
            })
    }

    private fun handleLoading() {
        clCreateTask.visibility = View.INVISIBLE
        lottiCreateTaskLoading.visibility = View.VISIBLE
        lottiCreateTaskLoading.playAnimation()
    }

    private fun handleSuccess(
        taskId: String,
        todoBody: String,
        todoDesc: String
    ) {
        taskImage = null
        val requestCode = taskId.generateRequestCode()
        startAlarmedNotification(
            requestCode,
            todoBody,
            todoDesc,
            calendar,
            alarmManager
        )
        lottiCreateTaskLoading.cancelAnimation()
        findNavController().navigate(R.id.action_taskCreateFragment_to_taskFragment)
    }

    private fun handleFailure() {
        lottiCreateTaskLoading.cancelAnimation()
        showToast(requireContext(), "Something went wrong")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            DATE_RESULT_CODE -> {
                val date = data?.getSerializableExtra(EXTRA_DATE).toString()
                pickedDate = date
                tvSelectDate.text = pickedDate
                openTimePicker(timePickerFragment)
            }
            TIME_RESULT_CODE -> {
                val time = data?.getSerializableExtra(EXTRA_TIME).toString()
                tvSelectTime.text = time
            }
            Activity.RESULT_OK -> {
                taskImage = data?.data
                tvSelectImage.text = taskImage.toString()
            }
        }
    }

    companion object {
        var taskImage: Uri? = null
        var pickedDate: String? = ""
    }
}
