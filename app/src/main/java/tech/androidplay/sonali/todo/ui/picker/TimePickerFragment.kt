package tech.androidplay.sonali.todo.ui.picker

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_HOUR
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_MINUTE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_TIME
import tech.androidplay.sonali.todo.utils.Constants.TIME_RESULT_CODE
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 02/Oct/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    @Inject
    lateinit var calendar: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return activity?.let { TimePickerDialog(activity, this, hour, minute, true) }!!
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        GLOBAL_HOUR = hour
        GLOBAL_MINUTE = minute
        val time = "${String.format("%02d", GLOBAL_HOUR)}:${String.format("%02d", GLOBAL_MINUTE)}"

        targetFragment?.let {
            val intent = Intent().putExtra(EXTRA_TIME, time)
            it.onActivityResult(targetRequestCode, TIME_RESULT_CODE, intent)
        } ?: return
    }

}