package tech.androidplay.sonali.todo.utils

import android.app.Activity
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
fun startInAppUpdate(activity: Activity) {

    val appUpdateManager = AppUpdateManagerFactory.create(activity)

    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {
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