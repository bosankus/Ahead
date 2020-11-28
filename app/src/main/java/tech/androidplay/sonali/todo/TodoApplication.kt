package tech.androidplay.sonali.todo

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:02 PM
 */

@HiltAndroidApp
class TodoApplication : Application() {

    companion object {
        var GLOBAL_YEAR = 2020
        var GLOBAL_MONTH = 1
        var GLOBAL_DAY = 1
        var GLOBAL_HOUR = 12
        var GLOBAL_MINUTE = 12
        var GLOBAL_SECOND = 0
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}