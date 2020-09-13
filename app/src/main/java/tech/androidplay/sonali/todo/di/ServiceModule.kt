package tech.androidplay.sonali.todo.di

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
            .setRequiredNetworkType(NetworkType.CONNECTED   )
            .build()
    }
}