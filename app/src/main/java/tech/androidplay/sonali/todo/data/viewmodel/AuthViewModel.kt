package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.GoogleAuthCredential
import tech.androidplay.sonali.todo.data.User
import tech.androidplay.sonali.todo.data.repository.AuthRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository by lazy { AuthRepository() }
    private lateinit var authenticatedUserLiveData: LiveData<User>

    fun signInWithGoogle(googleAuthCredential: GoogleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
    }

    fun signInWithEmailPassword(email: String, password: String) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithEmailPassword(email, password)
    }
}