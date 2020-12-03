package tech.androidplay.sonali.todo.utils.alarmutils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Dec/2020
 * Email: ankush@androidplay.in
 */
class DateTimeUtil @Inject constructor(private val currentDateTime: Calendar) {

    private var _dateTimeFormat = MutableLiveData<String>()
    val dateTimeFormat: LiveData<String>
        get() = _dateTimeFormat

    private var _epochFormat = MutableLiveData<Long>()
    val epochFormat: LiveData<Long>
        get() = _epochFormat

    fun openDateTimePicker(context: Context) {
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMin = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val pickDateTime = Calendar.getInstance()
                pickDateTime.set(year, month, day, hour, minute)
                _epochFormat.value = pickDateTime.time.time
                _dateTimeFormat.value = convertEpochMilliToDateTime(pickDateTime.time.time)

            }, startHour, startMin, true).show()
        }, startYear, startMonth, startDay).show()
    }

    // For UI: Showing epoch to correct format.
    @SuppressLint("SimpleDateFormat")
    fun convertEpochMilliToDateTime(epoch: Long?): String {
        epoch?.let {
            val date = Date(epoch)
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            return formatter.format(date)
        } ?: return "Error!"
    }
}