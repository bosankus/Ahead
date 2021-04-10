package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.text.format.DateUtils
import tech.androidplay.sonali.todo.utils.Constants.DEFAULT_TASK_TIME
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

/** Holds all the extension methods for handeling date time format & comaparing */

fun String.compareWithToday(): Int {
    val taskDate = this.toLocalDateTime()
    val currentDate = LocalDateTime.now()
    taskDate?.let {
        return when {
            taskDate.isEqual(currentDate) -> Constants.IS_EQUAL
            taskDate.isBefore(currentDate) -> Constants.IS_BEFORE
            taskDate.isAfter(currentDate) -> Constants.IS_AFTER
            else -> Constants.IS_ERROR
        }
    }
    return 2
}

fun String.toLocalDateTime(): LocalDateTime? {
    return if (this.isNotEmpty()) {
        val epochValue = this.toLong()
        Instant.ofEpochMilli(epochValue).atZone(ZoneId.systemDefault()).toLocalDateTime()
    } else Instant.ofEpochMilli(DEFAULT_TASK_TIME).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun LocalDateTime.beautifyDateTime(): String {
    val dateTime = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")
    return this.format(dateTime)
}

@SuppressLint("SimpleDateFormat")
fun Long.convertFromEpochTime(): String {
    val timeNow = System.currentTimeMillis()
    val givenTime = this
    val timeDayRelative = DateUtils.getRelativeTimeSpanString(
        givenTime,
        timeNow,
        DateUtils.DAY_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    )
    val hourFormatter = SimpleDateFormat("HH:mm a")
    val timeHour = hourFormatter.format(givenTime)
    return "$timeDayRelative at $timeHour"
}