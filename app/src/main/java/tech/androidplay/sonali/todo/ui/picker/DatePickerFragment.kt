package tech.androidplay.sonali.todo.ui.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import tech.androidplay.sonali.todo.TodoApplication
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_DAY
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_MONTH
import tech.androidplay.sonali.todo.TodoApplication.Companion.GLOBAL_YEAR
import tech.androidplay.sonali.todo.utils.Constants.DATE_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_DATE
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 02/Oct/2020
 * Email: ankush@androidplay.in
 */

@AndroidEntryPoint
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var calendar: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        return activity?.let {
            DatePickerDialog(it, this, year, month, day)
        }!!
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        calendar.set(year, month + 1, day)

        GLOBAL_YEAR = year
        GLOBAL_MONTH = month
        GLOBAL_DAY = day

        val date = "$day-${month + 1}-$year"

        targetFragment?.let {
            val intent = Intent().putExtra(EXTRA_DATE, date)
            it.onActivityResult(targetRequestCode, DATE_RESULT_CODE, intent)
        } ?: return

    }
}