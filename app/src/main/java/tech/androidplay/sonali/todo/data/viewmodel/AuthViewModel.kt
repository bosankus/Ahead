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

    private val authRepository: AuthRepository by lazy { AuthRepository }

    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _loginBtn = MutableLiveData(1)
    private var _isBusy = MutableLiveData(0)

    val email = _email
    val password = _password

    val loginBtn = _loginBtn
    val isBusy = _isBusy

    fun createAccountWithEmailPassword() {
        loginBtn.value = 0
        isBusy.value = 1
        authRepository.createWithEmailPassword(email.value.toString(), password.value.toString())
        authRepository.authLiveData.observeForever {
            if (it == 1) {
                isBusy.value = 0
            } else {
                isBusy.value = 0
                loginBtn.value = 1
            }
        }
    }

    fun loginWithEmailPassword() {
        loginBtn.value = 0
        isBusy.value = 1
        authRepository.loginWithEmailPassword(email.value.toString(), password.value.toString())
        authRepository.authLiveData.observeForever {
            if (it == 1) {
                isBusy.value = 0
            } else {
                isBusy.value = 0
                loginBtn.value = 1
            }
        }
    }

    fun sendPasswordResetEmail() {
        authRepository.sendPasswordResetEmail(email.value.toString())
        authRepository.authLiveData.observeForever {
            if (it == 1) {
                logMessage("Email send to ${email.value}")
            } else logMessage("Provide correct email ID")
        }
    }

    override fun onCleared() {
        super.onCleared()
        authRepository.authLiveData.removeObserver {
            logMessage("Observer cleared $it")
        }
        viewModelScope.cancel()
    }
}