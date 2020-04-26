package tech.androidplay.sonali.todo.utils

import android.util.Log

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/26/2020, 10:51 AM
 */
class Helper: Constants {
    fun logErrorMessage(errorMessage: String) {
        Log.d(TAG, errorMessage)
    }
}