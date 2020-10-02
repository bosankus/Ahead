package tech.androidplay.sonali.todo.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import tech.androidplay.sonali.todo.utils.Constants.DATE_RESULT_CODE
import tech.androidplay.sonali.todo.utils.Constants.EXTRA_DATE
import tech.androidplay.sonali.todo.utils.UIHelper.CalendarInstance
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 02/Oct/2020
 * Email: ankush@androidplay.in
 */

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val calendar: Calendar = CalendarInstance

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_WEEK)

        return activity?.let {
            DatePickerDialog(it, this, year, month, day)
        }!!
    }

    override fun onDateSet(view: DatePicker?, y: Int, m: Int, d: Int) {
        calendar.set(y, m, d)
        var date = ""
        date =
            if (m < 10)
                "$d.0$m.$y "
            else if (d < 10)
                "0$d.$m.$y "
            else if (m < 10 && d < 10)
                "0$d.0$m.$y "
            else "$d.$m.$y "

        targetFragment?.let {
            val intent = Intent().putExtra(EXTRA_DATE, date)
            it.onActivityResult(targetRequestCode, DATE_RESULT_CODE, intent)
        } ?: return

    }
}