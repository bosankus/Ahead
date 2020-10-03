package tech.androidplay.sonali.todo.utils

import android.os.Build

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 22/Aug/2020
 * Email: ankush@androidplay.in
 */
object Constants {

    const val GLOBAL_TAG = "Androidplay"

//    const val DISPLAY_IMAGE_URI = "DISPLAY_IMAGE_URI"

    const val FIRESTORE_COLLECTION = "Tasks"

    const val SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME"
    const val USER_DISPLAY_IMAGE = "USER_DISPLAY_IMAGE"

    const val TASK_DOC_ID = "TASK_DOC_ID"
    const val TASK_DOC_BODY = "TASK_DOC_BODY"
    const val TASK_DOC_DESC = "TASK_DOC_DESC"
    const val TASK_STATUS = "TASK_STATUS"
    const val TASK_REMINDER = "TASK_REMINDER"

    const val EXTRA_DATE = "EXTRA_DATE"
    const val EXTRA_TIME = "EXTRA_TIME"

    const val DATE_REQUEST_CODE = 100
    const val DATE_RESULT_CODE = 101

    const val TIME_REQUEST_CODE = 200
    const val TIME_RESULT_CODE = 201

    const val ACTION_SHOW_TASK_FRAGMENT = "ACTION_SHOW_TASK_FRAGMENT"
    const val NOTIFICATION_CHANNEL_ID = "think_channel"
    const val NOTIFICATION_CHANNEL_NAME = "think"
    const val NOTIFICATION_ID = 1

    const val NOTIFICATION_GROUP_ID = "111"
    const val NOTIFICATION_GROUP_NAME = "syncing"

    const val ANDROID_OREO = Build.VERSION_CODES.O
    const val ANDROID_10 = Build.VERSION_CODES.Q

    const val START_SYNC_SERVICE = "START_SYNC_SERVICE"
    const val STOP_SYNC_SERVICE  = "STOP_SYNC_SERVICE"

    const val TASK_TABLE = "TASK_TABLE"

    /*const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    const val STORAGE_PERMISSION_REQUEST_RATIONAL =
        "You need to accept location permissions to use this app"
    const val STORAGE_PERMISSION_REQUEST_CODE = 101*/
}