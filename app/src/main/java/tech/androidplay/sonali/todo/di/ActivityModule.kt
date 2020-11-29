package tech.androidplay.sonali.todo.di

import android.app.AlertDialog
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.ui.adapter.TodoAdapter

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun providesTodoAdapter(
        alertDialog: AlertDialog.Builder,
        viewModel: TaskViewModel
    ): TodoAdapter {
        return TodoAdapter(viewModel, alertDialog)
    }

    @Provides
    fun provideTaskViewModel(
        firebaseAuth: FirebaseAuth,
        firebaseRepository: FirebaseRepository
    ): TaskViewModel {
        return TaskViewModel(firebaseAuth, firebaseRepository)
    }

    @Provides
    fun providesAlertDialog(@ActivityContext context: Context): AlertDialog.Builder {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Do you want to delete the task")
        alertDialog.setCancelable(true)
        return alertDialog
    }
}