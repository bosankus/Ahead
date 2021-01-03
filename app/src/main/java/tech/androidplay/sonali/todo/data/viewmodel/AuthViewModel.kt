package tech.androidplay.sonali.todo.data.viewmodel

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */

@ExperimentalCoroutinesApi
class AuthViewModel @ViewModelInject constructor(private val dataSource: FirebaseRepository) :
    ViewModel() {

    fun createAccount(email: String, password: String): LiveData<ResultData<FirebaseUser>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty() || password.isEmpty() || password.length < 6) {
                emit(ResultData.Failed("Please check your input"))
            } else emit(dataSource.createAccount(email, password))
        }
    }

    fun loginUser(email: String, password: String): LiveData<ResultData<FirebaseUser>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty() || password.isEmpty() || password.length < 6) {
                emit(ResultData.Failed("Please check your input"))
            } else emit(dataSource.logInUser(email, password))
        }
    }

    fun resetPassword(email: String): LiveData<ResultData<String>> {
        return liveData {
            emit(ResultData.Loading)
            if (email.isEmpty()) {
                emit(ResultData.Failed("Please provide your email ID"))
            } else emit(dataSource.resetPassword(email))
        }
    }
}
