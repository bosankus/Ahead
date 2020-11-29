package tech.androidplay.sonali.todo.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import tech.androidplay.sonali.todo.ui.picker.DatePickerFragment
import tech.androidplay.sonali.todo.ui.picker.TimePickerFragment

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 06/Oct/2020
 * Email: ankush@androidplay.in
 */

object Extensions {

    fun Activity.hideKeyboard() {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.selectImage(context: Fragment) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    fun ImageView.loadImageCircleCropped(url: String) {
        Glide.with(this.context)
            .load(url)
            .circleCrop()
            .transform(CenterCrop(), RoundedCorners(20))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
            .clearOnDetach()
    }

    fun Fragment.openTimePicker(timePickerFragment: TimePickerFragment) {
        if (!timePickerFragment.isAdded) {
            timePickerFragment.setTargetFragment(this, Constants.TIME_REQUEST_CODE)
            timePickerFragment.show(parentFragmentManager, "TIME PICKER")
        }
    }

    fun Fragment.openDatePicker(datePickerFragment: DatePickerFragment) {
        if (!datePickerFragment.isAdded) {
            datePickerFragment.setTargetFragment(this, Constants.DATE_REQUEST_CODE)
            datePickerFragment.show(parentFragmentManager, "DATE PICKER")
        }
    }

    fun String.compareWith(newString: String): Boolean {
        return this.compareTo(newString) == 0
    }

}