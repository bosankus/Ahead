package tech.androidplay.sonali.todo.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */
class Helper : Constants {
    fun logErrorMessage(errorMessage: String) {
        Log.d(TAG, errorMessage)
    }

    fun showToast(context: Context, toastMessage: String) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
}