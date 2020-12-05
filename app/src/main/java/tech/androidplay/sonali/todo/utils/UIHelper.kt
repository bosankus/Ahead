package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import tech.androidplay.sonali.todo.utils.Constants.GLOBAL_TAG
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */

@SuppressLint("SimpleDateFormat")
object UIHelper {

    fun getCurrentTimestamp(): String =
        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(Date())

    fun logMessage(message: String) {
        Log.d(GLOBAL_TAG, message)
    }

    fun showToast(context: Context, toastMessage: String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, toastMessage: StringBuilder) {
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

}