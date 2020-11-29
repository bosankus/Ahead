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

        var time = ""
        time = (when {
            GLOBAL_MINUTE.toString().length == 1 -> {
                "$GLOBAL_HOUR:0$GLOBAL_MINUTE"
            }
            GLOBAL_HOUR.toString().length == 1 -> {
                "0$GLOBAL_HOUR:$GLOBAL_MINUTE"
            }
            GLOBAL_HOUR.toString().length == 1 && GLOBAL_MINUTE.toString().length == 1 -> {
                "0$GLOBAL_HOUR:0$GLOBAL_MINUTE"
            }
            else -> "$GLOBAL_HOUR:$GLOBAL_MINUTE"
        }).toString()

        targetFragment?.let {
            val intent = Intent().putExtra(EXTRA_TIME, time)
            it.onActivityResult(targetRequestCode, TIME_RESULT_CODE, intent)
        } ?: return
    }

}