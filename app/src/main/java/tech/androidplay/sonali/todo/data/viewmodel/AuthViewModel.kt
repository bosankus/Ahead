package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.Helper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    lateinit var authLiveData: MutableLiveData<Int>

    fun createAccountWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            authLiveData =
                authRepository.createAccountWithEmailPassword(email, password)
            logMessage("$authLiveData")
        }
    }

    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            authLiveData = authRepository.loginWithEmailPassword(email, password)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            authLiveData = authRepository.sendPasswordResetEmail(email)
        }
    }
}