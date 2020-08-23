package tech.androidplay.sonali.todo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.PermissionManager

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Aug/2020
 * Email: ankush@androidplay.in
 */

@Module
@InstallIn(ActivityComponent::class)
class AppModule {

    @Provides
    fun providesPermissionManager(): PermissionManager {
        return PermissionManager()
    }

    @Provides
    fun providesCacheManager(): CacheManager {
        return CacheManager()
    }

    @Provides
    fun providesTodoAdapter(): TodoAdapter {
        return TodoAdapter()
    }

    @Provides
    fun providesTaskRepository(): TaskRepository {
        return TaskRepository()
    }

    @Provides
    fun providesAuthRepository(): AuthRepository {
        return AuthRepository()
    }
}