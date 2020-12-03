package tech.androidplay.sonali.todo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:02 PM
 */

@HiltAndroidApp
class TodoApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}