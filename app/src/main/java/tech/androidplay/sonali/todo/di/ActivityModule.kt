@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection")

package tech.androidplay.sonali.todo.di

import android.app.AlertDialog
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import tech.androidplay.sonali.todo.utils.DateTimePicker
import tech.androidplay.sonali.todo.view.adapter.main_adapter.TodoAdapter
import tech.androidplay.sonali.todo.view.adapter.viewpager_adapter.ViewPagerAdapter
import tech.androidplay.sonali.todo.viewmodel.TaskViewModel
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

/** [ActivityModule] provides dependencies through activity level injections*/

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideDatetimePicker(calendar: Calendar) = DateTimePicker(calendar)

    @Provides
    fun providesTodoAdapter(): TodoAdapter = TodoAdapter()

    @Provides
    fun provideViewPagerAdapter(): ViewPagerAdapter = ViewPagerAdapter()

    @Provides
    fun providesAlertDialog(@ActivityContext context: Context): AlertDialog.Builder {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Do you want to delete the task")
        alertDialog.setCancelable(true)
        return alertDialog
    }
}