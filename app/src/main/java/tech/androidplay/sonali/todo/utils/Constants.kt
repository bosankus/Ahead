package tech.androidplay.sonali.todo.utils

import android.os.Build

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 22/Aug/2020
 * Email: ankush@androidplay.in
 */

/** Holds all the constants at one place */

object Constants {

    // In-App Update
    const val APP_UPDATE_REQ_CODE = 111

    // For default log tag
    const val QUOTE_API = "https://quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com/"

    // For work QuoteFetchWorker.kt
    const val QUOTE_AUTHOR = "author"
    const val QUOTE = "quote"
    const val QUOTE_WORKER_TAG = "quote_worker"
    const val QUOTE_WORKER_ID = 1001

    const val PLAY_STORE_LINK = "https://app.androidplay.in/ahead/android"
    const val GLOBAL_TAG = "Androidplay"

    // For firestore collection - Production
    const val TASK_COLLECTION = "Tasks"
    const val USER_COLLECTION = "Users"
    const val FEEDBACK_COLLECTION = "Feedbacks"
    const val DEFAULT_TASK_TIME: Long = 1299045057

    /*// For firestore collection - Test
    const val TASK_COLLECTION_TEST = "test_Tasks"
    const val USER_COLLECTION_TEST = "test_Users"
    const val FEEDBACK_COLLECTION_TEST = "test_Feedbacks"*/

    // For shared preference
    const val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    const val IS_FIRST_TIME = "SHARED_PREFERENCE_NAME"

    // For global navigation
    const val ACTION_SHOW_TASK_FRAGMENT = "ACTION_SHOW_TASK_FRAGMENT"
    /*const val ACTION_SHOW_TASK_EDIT_FRAGMENT = "ACTION_SHOW_TASK_EDIT_FRAGMENT"*/

    // For task alarm notification
    const val ALARM_ID = "ALARM_ID"
    const val ALARM_TEXT = "ALARM_TEXT"
    const val ALARM_DESCRIPTION = "ALARM_DESCRIPTION"

    // Return value for Date comparision
    const val IS_EQUAL = 0
    const val IS_BEFORE = -1
    const val IS_AFTER = 1
    const val IS_ERROR = 2

    // For notification channel
    const val NOTIFICATION_CHANNEL_ID = "think_channel"
    const val NOTIFICATION_CHANNEL_NAME = "think"
    const val NOTIFICATION_ID = "AHEAD"

    // For Android version check
    val DEVICE_ANDROID_VERSION = Build.VERSION.SDK_INT
    const val ANDROID_OREO = Build.VERSION_CODES.O
    const val ANDROID_S = Build.VERSION_CODES.S
    const val ANDROID_T = Build.VERSION_CODES.TIRAMISU

    // For opening document in EditTaskFragment
    const val TASK_DOC_ID = "TASK_DOC_ID"

    // For Task Priority
    const val TOP_PRIORITY = 1
    const val MEDIUM_PRIORITY = 2
    const val LOW_PRIORITY = 3

}