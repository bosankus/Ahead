package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.AuthResultData
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage
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

    private var createAccountLiveData: MutableLiveData<Int> = MutableLiveData()
    private var loginLiveData: MutableLiveData<Int> = MutableLiveData()
    private var passwordResetLiveData: MutableLiveData<Int> = MutableLiveData()

//    val firebaseUserLiveData by lazy { firebaseAuth.currentUser }

    // Creates Account
    fun createAccountWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { signUpTask ->
                if (signUpTask.isSuccessful) {
                    createAccountLiveData.value = 1
                } else {
                    logMessage("creation ${signUpTask.exception}")
                    createAccountLiveData.value = 0
                }
            }
        return createAccountLiveData
    }

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

    // Sending Password reset email
    fun sendPasswordResetEmail(email: String): MutableLiveData<Int> {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { resetTask ->
                logMessage(resetTask.exception.toString())
                if (resetTask.isSuccessful) {
                    passwordResetLiveData.value = 1
                } else passwordResetLiveData.value = 0
            }
        return passwordResetLiveData
    }


    // Sign out user
    /*fun signOut() {
        firebaseAuth.signOut()
    }*/
}