package tech.androidplay.sonali.todo.utils

import android.os.Build

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 22/Aug/2020
 * Email: ankush@androidplay.in
 */
object Constants {

    // For default log tag
    const val PLAY_STORE_LINK = "https://androidplay.in/29hQ"
    const val GLOBAL_TAG = "Androidplay"
    /*const val DATABASE_NAME = "think_db"*/

    const val QUOTE_API = "https://quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com/"

    // For firestore collection - Production
    const val TASK_COLLECTION = "Tasks"
    const val USER_COLLECTION = "Users"
    const val FEEDBACK_COLLECTION = "Feedbacks"

    const val TASK_ASSIGNED = 1
    const val TASK_ASSIGNED_ON_HOLD = 0

    /*// For firestore collection - Test
    const val TASK_COLLECTION_TEST = "test_Tasks"
    const val USER_COLLECTION_TEST = "test_Users"
    const val FEEDBACK_COLLECTION_TEST = "test_Feedbacks"*/

    // For shared preference
    const val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    const val IS_FIRST_TIME = "SHARED_PREFERENCE_NAME"

    // For navigation bundle
    const val TASK_DOC_ID = "TASK_DOC_ID"
    const val TASK_DOC_BODY = "TASK_DOC_BODY"
    const val TASK_DOC_DESC = "TASK_DOC_DESC"
    const val TASK_STATUS = "TASK_STATUS"
    const val TASK_DATE = "TASK_DATE"
    const val TASK_IMAGE_URL = "TASK_IMAGE_URL"

    // For global navigation
    const val ACTION_SHOW_TASK_FRAGMENT = "ACTION_SHOW_TASK_FRAGMENT"
    /*const val ACTION_SHOW_TASK_EDIT_FRAGMENT = "ACTION_SHOW_TASK_EDIT_FRAGMENT"*/

    // For task alarm notification
    /*const val ALARM_ID = "ALARM_ID"*/
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
    const val NOTIFICATION_ID = "NOTIFICATION_ID"

    // For Android version check
    val DEVICE_ANDROID_VERSION = Build.VERSION.SDK_INT
    const val ANDROID_OREO = Build.VERSION_CODES.O
    /*const val ANDROID_10 = Build.VERSION_CODES.Q*/

}