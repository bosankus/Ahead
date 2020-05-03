package tech.androidplay.sonali.todo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Switch
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Helper

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/3/2020, 12:22 PM
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var swAlarm: Switch
    private lateinit var frameDateTimePicker: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_task_bottom_sheet, container, false)
        swAlarm = view.findViewById(R.id.swAlarm)
        frameDateTimePicker = view.findViewById(R.id.frameDateTimePicker)
        frameDateTimePicker.visibility = View.GONE
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
            val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            val datePicker = datePickerBuilder.build()
            datePicker.show(parentFragmentManager, datePicker.toString())
            datePicker.addOnCancelListener {
                Helper().logErrorMessage("Cancelled")
            }
        }
    }
}