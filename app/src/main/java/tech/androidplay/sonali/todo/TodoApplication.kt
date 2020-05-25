package tech.androidplay.sonali.todo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.androidplay.sonali.todo.di.appModule

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:02 PM
 */

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TodoApplication)
            modules(listOf(appModule))
        }
    }
}