package tech.androidplay.sonali.todo.utils

import android.content.Context
import com.appsflyer.AppsFlyerLib
import javax.inject.Inject

/**Created by
Author: Ankush Bose
Date: 13,June,2021
 **/

enum class TrackingEvents {
    FIRST_VISIT,
    FIRST_LOGIN,
    FIRST_TASK,
    SECOND_VISIT,
    SECOND_TASK,
    FEEDBACK_STARTED,
    FEEDBACK_PROVIDED,
    APP_SHARED,
    UNINSTALLED_AFTER_FIRST_TASK,
    UNINSTALLED_AFTER_SECOND_TASK,
    UNINSTALLED_BEFORE_TASK_CREATION,
    UNINSTALLED_WITHIN_7DAYS,
    UNINSTALLED_WITHIN_15DAYS,
    UNINSTALLED_WITHIN_30DAYS,
}

class AppEventTracking @Inject constructor(
    private val context: Context,
    private val appsFlyerLib: AppsFlyerLib,
) {

    fun trackFirstVisit() {
        val event = HashMap<String, Any>()
        event["first_visit"] = true
        appsFlyerLib.logEvent(context, "firstVisit", event)
    }


    fun trackFirstLogin() {
        val event = HashMap<String, Any>()
        event["first_login"] = true
        appsFlyerLib.logEvent(context, "firstLogin", event)
    }


    fun trackFirstTaskCreation() {
        val event = HashMap<String, Any>()
        event["first_task"] = true
        appsFlyerLib.logEvent(context, "firstTaskCreate", event)
    }


    fun trackFeedbackIntention() {
        val event = HashMap<String, Any>()
        event["feedback_started"] = true
        appsFlyerLib.logEvent(context, "feedback", event)
    }


    fun trackFeedbackProvidedEvent() {
        val event = HashMap<String, Any>()
        event["feedback_provided"] = true
        appsFlyerLib.logEvent(context, "feedback", event)
    }


    fun trackAppSharing() {
        val event = HashMap<String, Any>()
        event["app_share"] = true
        appsFlyerLib.logEvent(context, "appShare", event)
    }


    fun trackUninstallAfterFirstTaskCreation() {
        val event = HashMap<String, Any>()
        event["uninstall_after_first_task"] = true
        appsFlyerLib.logEvent(context, "uninstallAfterFirstTask", event)
    }


    fun trackUninstallWithoutTaskCreation() {
        val event = HashMap<String, Any>()
        event["uninstall_without_task"] = true
        appsFlyerLib.logEvent(context, "uninstallWithoutTask", event)
    }
}