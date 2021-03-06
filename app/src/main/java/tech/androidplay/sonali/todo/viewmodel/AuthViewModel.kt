package tech.androidplay.sonali.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataSource: AuthRepository
) : ViewModel() {

    fun saveUserData(userDetails: FirebaseUser) {
        viewModelScope.launch {
            dataSource.saveUserDetailsInFireStore(userDetails)
        }
    }
}