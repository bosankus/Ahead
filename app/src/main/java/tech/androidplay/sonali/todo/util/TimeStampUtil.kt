package tech.androidplay.sonali.todo.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
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
    val currentDate: String by lazy { currentDate() }


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