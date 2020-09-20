package tech.androidplay.sonali.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_add_task_bottom_sheet.view.*
import kotlinx.android.synthetic.main.frame_date_time_picker.*
import kotlinx.android.synthetic.main.frame_date_time_picker.view.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.databinding.FragmentAddTaskBottomSheetBinding
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.showToast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private val taskViewModel: TaskViewModel by viewModels()

    private lateinit var binding: FragmentAddTaskBottomSheetBinding
    private lateinit var includeLayoutBinding: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBottomSheetBinding.inflate(inflater)
        includeLayoutBinding = binding.bottomSheetLayout

        setUpUi()

        clickListeners()

        return binding.root
    }

    private fun setUpUi() {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        includeLayoutBinding.frameDateTimePicker.visibility = View.GONE
        includeLayoutBinding.pbOpenCalender.visibility = View.GONE
    }

    private fun clickListeners() {
        binding.swAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val animationUtils =
                    AnimationUtils.loadAnimation(context, R.anim.fade_out_animation)
                includeLayoutBinding.frameDateTimePicker.startAnimation(animationUtils)
                includeLayoutBinding.frameDateTimePicker.visibility = View.VISIBLE
            } else if (!isChecked) {
                val animationUtils =
                    AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
                includeLayoutBinding.frameDateTimePicker.startAnimation(animationUtils)
                animationUtils.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        includeLayoutBinding.frameDateTimePicker.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {}
                })
            }
        }
        includeLayoutBinding.frameDateTimePicker.setOnClickListener {
            initiateDateTimePicker()
            includeLayoutBinding.pbOpenCalender.visibility = View.VISIBLE
        }
        binding.btCreateTask.setOnClickListener {
            if ((tvTaskInput.text.length) <= 0) tvTaskInput.error = "Cant't be empty mama!"
            else createTask()
        }
    }


    // pick date & time of task to be notified
    private fun initiateDateTimePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val datePicker = datePickerBuilder.build()
        datePicker.show(parentFragmentManager, datePicker.toString())
        datePicker.addOnCancelListener {
            pbOpenCalender.visibility = View.GONE
        }
        datePicker.addOnPositiveButtonClickListener {
            includeLayoutBinding.pbOpenCalender.visibility = View.GONE
            initiateTimePicker(datePicker.headerText)
        }
        datePicker.addOnNegativeButtonClickListener {
            includeLayoutBinding.pbOpenCalender.visibility = View.GONE
        }
    }

    // time picker
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initiateTimePicker(date: String) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            includeLayoutBinding.tvSelectDateTimeDesc.text =
                date + ", " + SimpleDateFormat("HH:mm").format(cal.time).toString()
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun createTask() {
        taskViewModel.createTask(
            tvTaskInput.text.toString(), tvSelectDateTimeDesc.text.toString()
        ).observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is ResultData.Loading -> {
                        binding.clCreateTask.visibility = View.INVISIBLE
                        binding.lottiCreateTaskLoading.visibility = View.VISIBLE
                        binding.lottiCreateTaskLoading.playAnimation()
                    }
                    is ResultData.Success -> {
                        binding.lottiCreateTaskLoading.cancelAnimation()
                        dismiss()
                    }
                    is ResultData.Failed -> showToast(requireContext(), "Something went wrong")
                }
            }
        })
    }

}