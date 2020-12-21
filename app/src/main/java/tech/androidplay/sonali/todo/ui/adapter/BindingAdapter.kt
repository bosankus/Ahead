package tech.androidplay.sonali.todo.ui.adapter

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.Extensions.convertFromEpochTime
import tech.androidplay.sonali.todo.utils.Extensions.removeStrikeThroughText
import tech.androidplay.sonali.todo.utils.Extensions.strikeThroughText

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 04/Dec/2020
 * Email: ankush@androidplay.in
 */


@BindingAdapter("strike_text")
fun TextView.strikeText(status: Boolean) {
    if (status) this.strikeThroughText()
    else this.removeStrikeThroughText()
}

@BindingAdapter("show")
fun View.visibility(status: Boolean) {
    if (status) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

// TODO
@BindingAdapter("day_name")
fun TextView.showDayName(date: String) {
    this.text = date.toLong().convertFromEpochTime()
}
