package tech.androidplay.sonali.todo.ui.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
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