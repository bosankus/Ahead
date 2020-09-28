package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCalenderTime
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var datePicker: MaterialDatePicker<Long>

    @Inject
    lateinit var calendar: Calendar

    @Inject
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    private val taskViewModel: TaskViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_task_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        clickListeners()
    }

    private fun setUpUi() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
    }

    private fun clickListeners() {
        tvSelectDateTimeDesc.setOnClickListener {
            initiateDateTimePicker()
        }
        btCreateTask.setOnClickListener {
            if ((tvTaskInput.text.length) <= 0) tvTaskInput.error = "Cant't be empty mama!"
            else createTask()
        }
    }


    // pick date & time of task to be notified
    private fun initiateDateTimePicker() {
        datePicker.show(parentFragmentManager, datePicker.toString())
        datePicker.addOnPositiveButtonClickListener {
            initiateTimePicker(datePicker.headerText)
        }
    }

    // time picker
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initiateTimePicker(date: String) {
        tvSelectDateTimeDesc.text = "$date, ${getCalenderTime(calendar)}"
        TimePickerDialog(
            context,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun createTask() {
        taskViewModel.createTask(
            tvTaskInput.text.toString(),
            tvTaskDescInput.text.toString(),
            tvSelectDateTimeDesc.text.toString()
        ).observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is ResultData.Loading -> {
                        clCreateTask.visibility = View.INVISIBLE
                        lottiCreateTaskLoading.visibility = View.VISIBLE
                        lottiCreateTaskLoading.playAnimation()
                    }
                    is ResultData.Success -> {
                        lottiCreateTaskLoading.cancelAnimation()
                        dismiss()
                    }
                    is ResultData.Failed -> showToast(requireContext(), "Something went wrong")
                }
            }
        })
    }

}