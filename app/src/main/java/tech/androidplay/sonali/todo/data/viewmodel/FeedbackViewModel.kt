package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class FeedbackViewModel @ExperimentalCoroutinesApi
@ViewModelInject constructor(
    firebaseAuth: FirebaseAuth,
    private val dataSource: FirebaseRepository
) : ViewModel() {

    private val currentUser = firebaseAuth.currentUser

    fun provideFeedback(topic: String, description: String): LiveData<ResultData<String>> {
        val hashMap = hashMapOf(
            "user" to currentUser?.email,
            "topic" to topic,
            "description" to description
        )
        return liveData {
            emit(ResultData.Loading)
            emit(dataSource.provideFeedback(hashMap))
        }
    }
}