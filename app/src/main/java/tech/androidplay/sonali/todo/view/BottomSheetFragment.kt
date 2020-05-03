package tech.androidplay.sonali.todo.view

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Helper
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var clCreateTask: ConstraintLayout
    private lateinit var swAlarm: Switch
    private lateinit var frameDateTimePicker: FrameLayout
    private lateinit var pickedTimeStamp: TextView
    private lateinit var lottieCreateTask: LottieAnimationView
    private lateinit var btnCreateTask: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_task_bottom_sheet, container, false)

        clCreateTask = view.findViewById(R.id.clCreateTask)
        swAlarm = view.findViewById(R.id.swAlarm)
        frameDateTimePicker = view.findViewById(R.id.frameDateTimePicker)
        frameDateTimePicker.visibility = View.GONE
        pickedTimeStamp = view.findViewById(R.id.tvSelectDateTimeDesc)
        lottieCreateTask = view.findViewById(R.id.lottiCreateTaskLoading)
        btnCreateTask = view.findViewById(R.id.btCreateTask)
        clickListeners()
        return view
    }


    private fun clickListeners() {
        swAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val animationUtils =
                    AnimationUtils.loadAnimation(context, R.anim.fade_out_animation)
                frameDateTimePicker.startAnimation(animationUtils)
                frameDateTimePicker.visibility = View.VISIBLE
            } else if (!isChecked) {
                val animationUtils =
                    AnimationUtils.loadAnimation(context, R.anim.fade_in_animation)
                frameDateTimePicker.startAnimation(animationUtils)
                animationUtils.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        frameDateTimePicker.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                })
            }
        }

        frameDateTimePicker.setOnClickListener {
            /**
             *  TODO: DatePicker opens very slowly. progressOpenCalendar is not working in View.GONE
             *  Learn and use coroutines to optimise this
             */
//            progressOpenCalendar.visibility = View.VISIBLE
            initiateDateTimePicker()
        }

        btnCreateTask.setOnClickListener {
            clCreateTask.visibility = View.INVISIBLE
            lottieCreateTask.visibility = View.VISIBLE
            lottieCreateTask.playAnimation()
        }
    }


    // pick date & time of task to be notified
    private fun initiateDateTimePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        val datePicker = datePickerBuilder.build()
        datePicker.show(parentFragmentManager, datePicker.toString())
        datePicker.addOnCancelListener {
            Helper().logErrorMessage("Cancelled. User clicked elsewhere")
        }
        datePicker.addOnPositiveButtonClickListener {
            Helper().logErrorMessage("Date String = ${datePicker.headerText}:: Date epoch value = $it")
            initiateTimePicker(datePicker.headerText)
        }
        datePicker.addOnNegativeButtonClickListener {
            Helper().logErrorMessage("Cancel button was pressed")
        }
    }

    // time picker
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initiateTimePicker(date: String) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            pickedTimeStamp.text =
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
}