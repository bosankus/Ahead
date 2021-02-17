package tech.androidplay.sonali.todo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    private val dataSource: AuthRepository
) : ViewModel() {

    fun saveUserData(userDetails: FirebaseUser) {
        viewModelScope.launch {
            dataSource.saveUserDetailsInFireStore(userDetails)
        }
    }
}