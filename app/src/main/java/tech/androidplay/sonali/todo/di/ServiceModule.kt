package tech.androidplay.sonali.todo.di

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tech.androidplay.sonali.todo.utils.Constants.SHARED_PREFERENCE_NAME
import tech.androidplay.sonali.todo.utils.Constants.USER_DISPLAY_IMAGE
import javax.inject.Singleton

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Aug/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun providesWorkManager(
        @ApplicationContext app: Context
    ): WorkManager {
        return WorkManager.getInstance(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideUserDisplayImage(sharedPreferences: SharedPreferences) =
        sharedPreferences.getString(USER_DISPLAY_IMAGE, "") ?: ""
}
