package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import tech.androidplay.sonali.todo.data.User
import tech.androidplay.sonali.todo.data.repository.AuthRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:51 AM
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository by lazy { AuthRepository() }
    lateinit var authenticatedUserLiveData: LiveData<User>
    lateinit var createdUserLiveData: LiveData<User>


    fun createAccountWithEmailPassword(email: String, password: String) {
        authenticatedUserLiveData = authRepository.createAccountWithEmailPassword(email, password)
    }

    fun signInWithEmailPassword(email: String, password: String) {
        authRepository.firebaseSignInWithEmailPassword(email, password)
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(account)
    }

    fun createUser(authenticatedUser: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser)
    }
}