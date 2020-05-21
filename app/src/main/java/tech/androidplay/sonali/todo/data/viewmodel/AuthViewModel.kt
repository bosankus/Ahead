package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository by lazy { AuthRepository() }
    lateinit var createAccountMutableLiveData: MutableLiveData<Int>
    lateinit var loginLiveData: MutableLiveData<Int>
    lateinit var passwordResetLiveData: MutableLiveData<String>


    fun createAccountWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            createAccountMutableLiveData =
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

//    fun signInWithGoogle(account: GoogleSignInAccount) {
//        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(account)
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}