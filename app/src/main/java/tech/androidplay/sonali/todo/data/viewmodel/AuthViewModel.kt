package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var createAccountLiveData: MutableLiveData<Int> = MutableLiveData()
    var loginLiveData: MutableLiveData<Int> = MutableLiveData()
    var passwordResetLiveData: MutableLiveData<Int> = MutableLiveData()

    fun createAccountWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            createAccountLiveData =
                authRepository.createAccountWithEmailPassword(email, password)
        }
    }

    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            loginLiveData = authRepository.loginWithEmailPassword(email, password)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            passwordResetLiveData = authRepository.sendPasswordResetEmail(email)
        }
    }
}