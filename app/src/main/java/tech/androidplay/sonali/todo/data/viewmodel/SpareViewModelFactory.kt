package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 7/17/2020, 12:46 AM
 */
@Suppress("UNCHECKED_CAST")
class SpareViewModelFactory<T>(private val creator: T): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SpareViewModel(creator.toString()) as T
    }
}