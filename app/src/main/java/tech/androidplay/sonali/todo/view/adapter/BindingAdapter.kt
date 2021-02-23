package tech.androidplay.sonali.todo.view.adapter

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.utils.UIHelper.removeStrikeThroughText
import tech.androidplay.sonali.todo.utils.UIHelper.strikeThroughText
import tech.androidplay.sonali.todo.utils.convertFromEpochTime


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

/*@BindingAdapter("animation_view")
fun View.animateButton(status: Boolean) {
    val fadeInAnim = AnimationUtils.loadAnimation(this.context, R.anim.fade_in_animation)
    val fadeOutAnim = AnimationUtils.loadAnimation(this.context, R.anim.fade_out_animation)
    if (status) viewAnimation(this, fadeInAnim, false)
    else viewAnimation(this, fadeOutAnim, true)
}*/

/*@BindingAdapter("lottie_animation")
fun View.animateLottie(status: Boolean) {
    viewAnimation(this, null, status)
}*/
