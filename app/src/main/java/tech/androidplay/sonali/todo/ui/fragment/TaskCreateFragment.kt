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
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.TodoApplication
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.picker.DatePickerFragment
import tech.androidplay.sonali.todo.ui.picker.TimePickerFragment
import tech.androidplay.sonali.todo.utils.Constants.DATE_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_DATE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_TIME
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
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

@AndroidEntryPoint
class TaskCreateFragment : Fragment(R.layout.fragment_task_create) {

    @Inject
    lateinit var app: TodoApplication

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
        tvSelectDateDesc.setOnClickListener { openDatePicker(datePickerFragment) }
        tvSelectTimeDesc.setOnClickListener { openTimePicker(timePickerFragment) }
        tvSelectImage.setOnClickListener { selectImage(this) }
        btCreateTask.setOnClickListener {
            if ((tvTaskInput.text.length) <= 0) tvTaskInput.error = "Cant't be empty!"
            else createTask()
        }
    }


    private fun createTask() {
        val todoBody = tvTaskInput.text.toString()
        val todoDesc = tvTaskDescInput.text.toString()
        val todoDate = tvSelectDateDesc.text.toString()
        val todoTime = tvSelectTimeDesc.text.toString()
        taskViewModel.createTask(todoBody, todoDesc, todoDate, todoTime, taskImage)
            .observe(viewLifecycleOwner, {
                it?.let {
                    when (it) {
                        is ResultData.Loading -> {
                            clCreateTask.visibility = View.INVISIBLE
                            lottiCreateTaskLoading.visibility = View.VISIBLE
                            lottiCreateTaskLoading.playAnimation()
                        }
                        is ResultData.Success -> {
                            taskImage = null
                            val requestCode = it.data!!.generateRequestCode()
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
                        is ResultData.Failed -> {
                            lottiCreateTaskLoading.cancelAnimation()
                            showToast(requireContext(), "Something went wrong")
                        }
                    }
                }
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            DATE_RESULT_CODE -> {
                val date = data?.getSerializableExtra(EXTRA_DATE).toString()
                pickedDate = date
                tvSelectDateDesc.text = pickedDate
            }
            TIME_RESULT_CODE -> {
                val time = data?.getSerializableExtra(EXTRA_TIME).toString()
                tvSelectTimeDesc.text = time
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
