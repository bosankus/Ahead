package tech.androidplay.sonali.todo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.repository.FeedbackRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class FeedbackViewModel @ExperimentalCoroutinesApi
@ViewModelInject constructor(private val dataSource: FeedbackRepository) : ViewModel() {

    private val _currentUser = dataSource.currentUser
    private val userEmailId get() = _currentUser?.email

    fun provideFeedback(topic: String, description: String): LiveData<ResultData<String>> {
        val hashMap = hashMapOf(
            "user" to userEmailId,
            "topic" to topic,
            "description" to description
        )
        return liveData {
            emit(ResultData.Loading)
            emit(dataSource.provideFeedback(hashMap))
        }
    }
}