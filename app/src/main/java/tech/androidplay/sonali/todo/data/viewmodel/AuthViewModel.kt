package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
class AuthViewModel @ViewModelInject constructor(private val firebaseRepository: FirebaseRepository) :
    ViewModel() {

    fun createAccount(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(ResultData.Loading)
        emit(firebaseRepository.createAccount(email, password))
    }

    fun loginUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(ResultData.Loading)
        emit(firebaseRepository.logInUser(email, password))
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        firebaseRepository.resetPassword(email)
    }

    fun logoutUser() = viewModelScope.launch {
        firebaseRepository.signOut()
    }
}
