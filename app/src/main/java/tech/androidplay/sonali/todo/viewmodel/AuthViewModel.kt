package tech.androidplay.sonali.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataSource: TodoRepository
) : ViewModel() {

    fun saveUserData(userDetails: FirebaseUser) {
        viewModelScope.launch {
            dataSource.saveUser(userDetails)
        }
    }
}