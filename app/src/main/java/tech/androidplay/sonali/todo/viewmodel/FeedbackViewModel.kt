package tech.androidplay.sonali.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@HiltViewModel
class FeedbackViewModel @Inject constructor(private val dataSource: TodoRepository) :
    ViewModel() {

    fun provideFeedback(topic: String, description: String): LiveData<ResultData<String>> =
        liveData {
            emit(ResultData.Loading)
            emit(dataSource.provideFeedback(topic, description))
        }
}