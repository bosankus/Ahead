package tech.androidplay.sonali.todo.utils

import android.content.IntentSender
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.utils.Constants.APP_UPDATE_REQ_CODE
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
import tech.androidplay.sonali.todo.view.activity.MainActivity

/**Created by
Author: Ankush Bose
Date: 13,May,2021
 **/


@ExperimentalCoroutinesApi
fun startInAppUpdate(activity: MainActivity) {

    val appUpdateManager = AppUpdateManagerFactory.create(activity)

    // Returns an intent object that you use to check for an update.
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    // Checks that the platform will allow the specified type of update.
    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            // This example applies an immediate update. To apply a flexible update
            // instead, pass in AppUpdateType.FLEXIBLE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {
            // Request the update.
            try {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    APP_UPDATE_REQ_CODE
                )
            } catch (exception: IntentSender.SendIntentException) {
                logMessage("InAppUpdate Error: ${exception.localizedMessage}")
            }
        }
    }

}