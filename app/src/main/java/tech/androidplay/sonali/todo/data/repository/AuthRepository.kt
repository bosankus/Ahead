package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.AuthResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */

// Google Sign in:
// Client ID: 768552120145-abp9v8hb05n3skksjg44jgblsnkudan7.apps.googleusercontent.com
// Client Secret: GiFS-xS6M9M3-kmY8mKgclpV

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    private var passwordResetLiveData: MutableLiveData<Int> = MutableLiveData()

//    val firebaseUserLiveData by lazy { firebaseAuth.currentUser }

    suspend fun createAccount(email: String, password: String): AuthResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            AuthResultData.Success(response.user)
        } catch (e: Exception) {
            AuthResultData.Failed(e.message)
        }
    }

    suspend fun loginUser(email: String, password: String): AuthResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            AuthResultData.Success(response.user)
        } catch (e: Exception) {
            AuthResultData.Failed(e.message)
        }
    }

    fun resetPassword(email: String) {
        try {
            firebaseAuth
                .sendPasswordResetEmail(email)
        } catch (e: Exception) {
            AuthResultData.Failed(e.message)
        }
    }


    // Sign out user
    /*fun signOut() {
        firebaseAuth.signOut()
    }*/
}