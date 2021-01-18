package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.ui.HorizontalMarginItemDecoration
import tech.androidplay.sonali.todo.utils.Constants.GLOBAL_TAG
import java.time.Instant

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */

@SuppressLint("SimpleDateFormat")
object UIHelper {

    fun getCurrentTimestamp(): String = Instant.now().toEpochMilli().toString()

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

    fun Activity.hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) view = View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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

    fun CharSequence.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun ViewPager2.decoratePages() {
        this.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.10f * kotlin.math.abs(position))
            /*page.alpha = 0.10f + (1 - kotlin.math.abs(position))*/
        }
        this.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(
            this.context,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        this.addItemDecoration(itemDecoration)
    }
}