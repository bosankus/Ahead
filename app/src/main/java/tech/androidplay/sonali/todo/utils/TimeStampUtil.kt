package tech.androidplay.sonali.todo.utils

import android.os.Build
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/20/2020, 3:01 AM
 */

class TimeStampUtil {

    val currentTime: String by lazy {
        SimpleDateFormat(
            "HH:mm",
            Locale.getDefault()
        ).format(Date())
    }

    val currentLocalDate: String by lazy { showDateMonthYear() }

    val currentDate: String by lazy { currentDate() }

    private fun showDateMonthYear(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private fun currentDate(): String {
        return when {
            isToday() -> "Today"
            isYesterday() -> "Yesterday"
            else -> showDate()
        }
    }

    private fun isToday(date: Long = Date().time): Boolean {
        return DateUtils.isToday(date)

    }

    private fun isYesterday(date: Long = Date().time): Boolean {
        return DateUtils.isToday(date - DateUtils.DAY_IN_MILLIS)
    }

    private fun showDate(): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
    }
}