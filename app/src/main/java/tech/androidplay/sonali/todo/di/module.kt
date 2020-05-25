package tech.androidplay.sonali.todo.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:03 PM
 */

val appModule = module {
    factory { AuthRepository() }
    viewModel { AuthViewModel(get()) }
    single { TaskRepository() }
    viewModel { TaskViewModel(get()) }
}