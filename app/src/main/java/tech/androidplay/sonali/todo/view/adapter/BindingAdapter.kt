package tech.androidplay.sonali.todo.view.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.UIHelper.removeStrikeThroughText
import tech.androidplay.sonali.todo.utils.UIHelper.showSnack
import tech.androidplay.sonali.todo.utils.UIHelper.strikeThroughText


/**
 * Created by Androidplay
 * Author: Ankush
 * On: 04/Dec/2020
 * Email: ankush@androidplay.in
 */

// Task Fragment

@BindingAdapter("strike_text")
fun TextView.strikeText(status: Boolean) {
    if (status) this.strikeThroughText()
    else this.removeStrikeThroughText()
}

@BindingAdapter("show")
fun View.visibility(status: Boolean) {
    if (status) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

@BindingAdapter("day_name")
fun TextView.showDayName(date: String) {
    this.text = date.toLong().convertFromEpochTime()
}

@BindingAdapter("background_color")
fun View.backgroundColor(priority: Int) {
    this.background = when (priority) {
        0 -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_gradient_low)
        1 -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_gradient_medium)
        2 -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_gradient_high)
        else -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_gradient_low)
    }
}


// Task Edit Fragment

@BindingAdapter("isProgressVisible")
fun View.isProgressVisible(responseState: ResultData<*>) {
    visibility = if (responseState is ResultData.Loading) View.VISIBLE
    else View.GONE
}

@BindingAdapter("isErrorVisible")
fun View.isErrorVisible(responseState: ResultData<*>) {
    visibility = if (responseState is ResultData.Failed) View.VISIBLE
    else View.GONE
}

@BindingAdapter("isResponseReceived")
fun View.isResponseReceived(responseState: ResultData<*>) {
    visibility = if (responseState is ResultData.Success<*>) View.VISIBLE
    else View.GONE
}

@BindingAdapter("setNotificationDateTime")
fun TextView.setNotificationDateTime(data: String?) {
    data?.let {
        val dataToDateTimeFormat = it.toLocalDateTime()?.beautifyDateTime()
        text = when {
            it.compareWithToday() == IS_BEFORE -> resources.getString(
                R.string.prompt_past_notification,
                dataToDateTimeFormat ?: "Unknown Error"
            )
            it.compareWithToday() == IS_AFTER -> resources.getString(
                R.string.prompt_future_notification,
                dataToDateTimeFormat ?: "Unknown Error"
            )
            else -> resources.getString(
                R.string.prompt_past_no_notification,
                dataToDateTimeFormat ?: "Unknown Error"
            )
        }
    }
}

@BindingAdapter("isNoImageTextVisible")
fun TextView.isNoImageTextVisible(url: String?) {
    visibility = if (url?.isNotEmpty() == true) View.GONE else View.VISIBLE
}

@BindingAdapter("showImage")
fun ImageView.showImage(url: String?) {
    if (url?.isNotEmpty() == true) {
        this.apply {
            loadImage(url)
            visibility = View.VISIBLE
        }
    } else this.visibility = View.GONE
}

@BindingAdapter("makeTint")
fun ImageView.makeTint(url: String?) {
    if (url?.isNotEmpty() == true) this.setTint(R.color.white)
    else this.setTint(R.color.dribblePink)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("isImageUploading")
fun View.isImageUploading(responseState: ResultData<*>) {
    if (responseState is ResultData.Loading)
        showSnack(this, "Uploading...")
    else if (responseState is ResultData.Success)
        showSnack(this, "Great! image uploaded")
    else if (responseState is ResultData.Failed)
        showSnack(this, "Check your connection")
}

@BindingAdapter("isDeleting")
fun TextView.isDeleting(responseState: ResultData<*>) {
    if (responseState is ResultData.Loading)
        text = "Deleting task..."
    else if (responseState is ResultData.Failed)
        showSnack(this.rootView, "Deleted")
}

@BindingAdapter("isUpdating")
fun TextView.isUpdating(responseState: ResultData<*>) {
    when (responseState) {
        is ResultData.DoNothing -> text = "Save"
        is ResultData.Loading -> text = "Saving..."
        is ResultData.Failed -> text = "Try again!"
        is ResultData.Success -> {
            text = "Save"
            showSnack(this.rootView, "Saved successfully")
        }
    }
}


