package tech.androidplay.sonali.todo.view.adapter

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.*
import tech.androidplay.sonali.todo.utils.Constants.IS_AFTER
import tech.androidplay.sonali.todo.utils.Constants.IS_BEFORE
import tech.androidplay.sonali.todo.utils.Constants.LOW_PRIORITY
import tech.androidplay.sonali.todo.utils.Constants.MEDIUM_PRIORITY
import tech.androidplay.sonali.todo.utils.Constants.TOP_PRIORITY
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
fun TextView.showDayName(date: String?) {
    this.text = if (date.isNullOrEmpty()) "" else date.toLong().convertFromEpochTime()
}

@BindingAdapter("priority_text")
fun TextView.setPriorityText(priority: Int) {
    this.text = when (priority) {
        LOW_PRIORITY -> "Low Priority"
        MEDIUM_PRIORITY -> "Medium Priority"
        TOP_PRIORITY -> "Top priority"
        else -> "Add Priority"
    }
}

@BindingAdapter("background_color")
fun View.backgroundColor(priority: Int) {
    this.background = when (priority) {
        LOW_PRIORITY -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_grad_low)
        MEDIUM_PRIORITY -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_grad_medium)
        TOP_PRIORITY -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_grad_high)
        else -> ContextCompat.getDrawable(this.context, R.drawable.bg_card_grad_low)
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
    when (responseState) {
        is ResultData.Loading -> showSnack(this, "Uploading...")
        is ResultData.Success -> showSnack(this, "Great! image uploaded")
        is ResultData.Failed -> showSnack(this, "Check your connection")
        else -> showSnack(this, "Image uploading failed. Please retry.")
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("isDeleting")
fun TextView.isDeleting(responseState: ResultData<*>) {
    if (responseState is ResultData.Loading)
        text = "Deleting task..."
    else if (responseState is ResultData.Failed)
        showSnack(this.rootView, "Deleted")
}

@SuppressLint("SetTextI18n")
@BindingAdapter("isUpdating")
fun TextView.isUpdating(responseState: ResultData<*>) {
    text = when (responseState) {
        is ResultData.DoNothing -> "Save"
        is ResultData.Loading -> "Saving..."
        is ResultData.Failed -> "Try again!"
        is ResultData.Success -> {
            "Save"
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("checkBoxText")
fun AppCompatCheckBox.checkBoxText(status: Boolean) {
    text = if (status) "Marked as completed"
    else "Mark as complete"
}


// Create Task Fragment

@BindingAdapter("textChangedListener")
fun EditText.bindTextWatcher(textWatcher: TextWatcher) {
    this.addTextChangedListener(textWatcher)
}

@BindingAdapter("emailAvailabilityStatus", "currentUserEmail", "emailUnderCheck")
fun TextView.showAvailability(
    responseState: ResultData<*>,
    currentUserEmail: String,
    emailUnderCheck: String?
) {
    text = if (!emailUnderCheck.isNullOrEmpty() && currentUserEmail.isNotEmpty()) {
        when (responseState) {
            is ResultData.Loading -> context.resources.getString(R.string.Checking)
            is ResultData.Success -> {
                if (currentUserEmail == emailUnderCheck)
                    context.resources.getString(R.string.self_assign_error)
                else context.resources.getString(R.string.assign_user_added)
            }
            else -> context.resources.getString(R.string.assign_no_user)
        }
    } else if (emailUnderCheck?.isEmpty() == true) "Assignee"
    else "Assignee"
}
