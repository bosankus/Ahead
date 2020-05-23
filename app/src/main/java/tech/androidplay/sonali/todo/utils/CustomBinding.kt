package tech.androidplay.sonali.todo.utils

import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/23/2020, 3:58 AM
 */

@BindingAdapter("lottie_visibility")
fun setVisibility(lottieAnimationView: LottieAnimationView, visibility: Int) {
    lottieAnimationView.visibility = visibility
}

@BindingAdapter("button_visibility")
fun setVisibility(appCompatButton: AppCompatButton, visibility: Int) {
    appCompatButton.visibility = visibility
}