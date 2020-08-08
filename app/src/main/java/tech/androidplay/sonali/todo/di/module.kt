package tech.androidplay.sonali.todo.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.data.viewmodel.AuthViewModel
import tech.androidplay.sonali.todo.data.viewmodel.TaskViewModel
import tech.androidplay.sonali.todo.utils.CacheManager
import tech.androidplay.sonali.todo.utils.PermissionManager
import tech.androidplay.sonali.todo.view.MainActivity

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/25/2020, 8:03 PM
 */

val appModule = module {
    factory { AuthRepository() }
    viewModel { AuthViewModel(get()) }
    viewModel { TaskViewModel(get()) }
    factory { TodoListAdapter(get()) }
    factory { CacheManager() }
    single { PermissionManager() }
    single { TaskRepository() }
}