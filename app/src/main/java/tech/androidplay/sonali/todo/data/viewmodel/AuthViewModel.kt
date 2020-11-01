package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    fun createAccount(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(ResultData.Loading)
        emit(authRepository.createAccount(email, password))
    }

    fun loginUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(ResultData.Loading)
        emit(authRepository.loginUser(email, password))
    }

    fun resetPassword(email: String) {
        authRepository.resetPassword(email)
    }
}