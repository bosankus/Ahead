package tech.androidplay.sonali.todo.utils

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/23/2020, 3:58 AM
 */

@BindingAdapter("lottie_visibility")
fun setVisibility(lottieAnimationView: LottieAnimationView, number: Int) {
    lottieAnimationView.visibility = if (number == 0) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("button_visibility")
fun setVisibility(appCompatButton: AppCompatButton, number: Int) {
    appCompatButton.visibility = if (number == 0) View.INVISIBLE else View.VISIBLE
}