package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

//    val firebaseUser by lazy { authRepository.firebaseUserLiveData }

    fun createAccount(email: String, password: String): LiveData<ResultData<FirebaseUser?>> {
        return liveData {
            emit(ResultData.Loading)
            emit(authRepository.createAccount(email, password))
        }
    }

    fun loginUser(email: String, password: String): LiveData<ResultData<FirebaseUser?>> {
        return liveData {
            emit(ResultData.Loading)
            emit(authRepository.loginUser(email, password))
        }
    }

    fun resetPassword(email: String) {
        authRepository.resetPassword(email)
    }
}