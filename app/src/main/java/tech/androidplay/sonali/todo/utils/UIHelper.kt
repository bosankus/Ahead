package tech.androidplay.sonali.todo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */

object UIHelper {

    private val TAG = "Androidplay"
    var networkFlag: Boolean = false

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTimestamp(): String =
        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(Date())

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String = SimpleDateFormat("EEE, MMM d, ''yy").format(Date())

    fun logMessage(message: String) {
        Log.d(TAG, message)
    }

    fun showToast(context: Context?, toastMessage: String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun viewAnimation(view: View, animation: Animation?, visibility: Boolean) {
        if (animation != null) view.startAnimation(animation)
        if (visibility) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE
    }

}