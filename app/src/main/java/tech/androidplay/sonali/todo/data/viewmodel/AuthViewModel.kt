package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.Helper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository by lazy { AuthRepository() }
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val email = _email
    val password = _password

    lateinit var createAccountMutableLiveData: MutableLiveData<Int>
    lateinit var loginLiveData: MutableLiveData<Int>
    lateinit var passwordResetLiveData: MutableLiveData<String>


    fun createAccountWithEmailPassword() {
        createAccountMutableLiveData =
            authRepository.createAccountWithEmailPassword(email.value.toString(), password.value.toString())
    }

    fun loginWithEmailPassword() {
        logMessage("From AuthViewModel: " + email.value.toString() + " " + password.value.toString())
        loginLiveData = authRepository.loginWithEmailPassword(email.value.toString(), password.value.toString())
    }

    fun sendPasswordResetEmail() {
        passwordResetLiveData = authRepository.sendPasswordResetEmail(email.value.toString())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}