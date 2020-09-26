package tech.androidplay.sonali.todo.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import tech.androidplay.sonali.todo.utils.Constants.GLOBAL_TAG
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */

object UIHelper {

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimestamp(): String =
        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Date())

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String = SimpleDateFormat("EEE, MMM d, ''yy").format(Date())

    fun logMessage(message: String) {
        Log.d(GLOBAL_TAG, message)
    }

    fun showToast(context: Context, toastMessage: String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun showSnack(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .show()
    }

    fun viewAnimation(view: View, animation: Animation?, visibility: Boolean) {
        if (animation != null) view.startAnimation(animation)
        if (visibility) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE
    }

    fun strikeText(textView: TextView) {
        val text = textView.text.toString()
        val spannable = SpannableString(text)
        spannable.setSpan(StrikethroughSpan(), 0, text.length, 0)
        textView.text = spannable
    }

    fun removeStrikeText(textView: TextView) {
        val text = textView.text.toString()
        val spannable = SpannableString(text)
        spannable.removeSpan(StrikethroughSpan())
        textView.text = spannable
    }

    fun FragmentActivity.selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    fun View.startCircularReveal(fromLeft: Boolean) {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int,
                oldRight: Int, oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                // TODO: Inject this from arguments
                val cx = if (fromLeft) v.left else v.right
                val cy = v.bottom
                val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
                ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, radius.toFloat()).apply {
                    interpolator = DecelerateInterpolator(2f)
                    duration = 1000
                    start()
                }
            }
        })
    }

    fun View.exitCircularReveal(exitX: Int, exitY: Int, block: () -> Unit) {
        val startRadius = Math.hypot(this.width.toDouble(), this.height.toDouble())
        ViewAnimationUtils.createCircularReveal(this, exitX, exitY, startRadius.toFloat(), 0f)
            .apply {
                duration = 350
                interpolator = DecelerateInterpolator(1f)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        block()
                        super.onAnimationEnd(animation)
                    }
                })
                start()
            }
    }

    fun View.findLocationOfCenterOnTheScreen(): IntArray {
        val positions = intArrayOf(0, 0)
        getLocationInWindow(positions)
        // Get the center of the view
        positions[0] = positions[0] + width / 2
        positions[1] = positions[1] + height / 2
        return positions
    }
}