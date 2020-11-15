package tech.androidplay.sonali.todo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 * *************************
 * Test Cases:
 * The input for creating account and login is not valid if:
 * ...the username (i.e email id) already exists
 * ...the password is less than 6 letters
 * ...the username/password field is empty
 * *************************
 * The input for resetting password is invalid if:
 * ...the username is not registered, no reset email will go
 * ...the username field is empty
 */

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun createAccount(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            ResultData.Success(response.user)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    suspend fun loginUser(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            ResultData.Success(response.user)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    fun resetPassword(email: String) {
        try {
            firebaseAuth
                .sendPasswordResetEmail(email)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }


    // Sign out user
    /*fun signOut() {
        firebaseAuth.signOut()
    }*/
}