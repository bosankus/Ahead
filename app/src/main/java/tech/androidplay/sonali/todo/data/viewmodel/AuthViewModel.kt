package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import tech.androidplay.sonali.todo.utils.AuthResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel @ViewModelInject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    var passwordResetLiveData: MutableLiveData<Int> = MutableLiveData()

//    val firebaseUser by lazy { authRepository.firebaseUserLiveData }

    fun createAccount(email: String, password: String): LiveData<AuthResultData<FirebaseUser?>> {
        return liveData {
            emit(AuthResultData.Loading)
            emit(authRepository.createAccount(email, password))
        }
    }

    fun loginUser(email: String, password: String): LiveData<AuthResultData<FirebaseUser?>> {
        return liveData {
            emit(AuthResultData.Loading)
            emit(authRepository.loginUser(email, password))
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            passwordResetLiveData = authRepository.sendPasswordResetEmail(email)
        }
    }
}