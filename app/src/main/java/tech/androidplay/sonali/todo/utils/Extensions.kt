package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.dhaval2404.imagepicker.ImagePicker
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
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    fun ImageView.loadImageCircleCropped(url: String) {
        Glide.with(this.context)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(20))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)

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
        val date = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a")
        return this.format(date)
    }
}