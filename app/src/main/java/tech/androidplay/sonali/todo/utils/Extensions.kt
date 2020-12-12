package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.text.SpannableString
import android.text.format.DateUtils
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.RoundedCornersTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

@SuppressLint("SimpleDateFormat")
object Extensions {

    // View ext. functions

    fun Activity.hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(500)
            .maxResultSize(500, 500)
            .start()
    }

    fun ImageView.loadImageCircleCropped(url: String?) {
        url.let {
            this.load(it) {
                crossfade(true)
                transformations(
                    RoundedCornersTransformation(
                        20.0F, 20.0F, 20.0F, 20.0F
                    )
                )
            }
        }
    }

    fun ImageView.setTint(colorId: Int) {
        ImageViewCompat.setImageTintList(
            this, ColorStateList.valueOf(
                ContextCompat.getColor(this.context, colorId)
            )
        )
    }

    fun TextView.strikeThroughText() {
        val text = this.text.toString()
        val spannable = SpannableString(text)
        spannable.setSpan(StrikethroughSpan(), 0, text.length, 0)
        this.text = spannable
    }

    fun TextView.removeStrikeThroughText() {
        val text = this.text.toString()
        val spannable = SpannableString(text)
        spannable.removeSpan(StrikethroughSpan())
        this.text = spannable
    }

    fun Fragment.shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        val shareText =
            "Let's get your tasks noted and reminded to you, just with little ease. Download now ${Constants.PLAY_STORE_LINK}"
        val shareSubText = "Think Ahead - Personal Task Tracker"
        sharingIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, shareSubText)
            putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(this, "Share via"))
        }
    }

    // Other Ext. functions

    fun String.compareWithToday(): Int {
        val taskDate = this.toLocalDateTime()
        val currentDate = LocalDateTime.now()
        taskDate?.let {
            return when {
                taskDate.isEqual(currentDate) -> 0
                taskDate.isBefore(currentDate) -> -1
                taskDate.isAfter(currentDate) -> 1
                else -> 2
            }
        }
        return 2
    }

    fun String.toLocalDateTime(): LocalDateTime? {
        val epoch = this
        return Instant.ofEpochMilli(epoch.toLong()).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun LocalDateTime.beautifyDateTime(): String {
        val dateTime = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")
        return this.format(dateTime)
    }

    fun LocalDateTime.beautifyTime(): String {
        val time = DateTimeFormatter.ofPattern("hh:mm a")
        return this.format(time)
    }

    fun Long.convertFromEpochTime(): String {
        val timeNow = System.currentTimeMillis()
        val giveTime = this
        val timeDayRelative = DateUtils.getRelativeTimeSpanString(
            giveTime,
            timeNow,
            DateUtils.DAY_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
        val hourFormatter = SimpleDateFormat("HH:mm a")
        val timeHour = hourFormatter.format(giveTime)
        return "$timeDayRelative at $timeHour"
    }

}