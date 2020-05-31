package tech.androidplay.sonali.todo.databinding

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import tech.androidplay.sonali.todo.utils.UIHelper.removeStrikeText
import tech.androidplay.sonali.todo.utils.UIHelper.strikeText

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/29/2020, 3:12 AM
 */

@BindingAdapter("set_alpha")
fun setAlpha(constraintLayout: ConstraintLayout, status: Boolean) {
    if (status) constraintLayout.alpha = 0.5F
    else constraintLayout.alpha = 1F
}

@BindingAdapter("strike_task")
fun strikeTask(textView: TextView, status: Boolean) {
    if (status) strikeText(textView)
    else removeStrikeText(textView)
}