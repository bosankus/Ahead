package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import tech.androidplay.sonali.todo.data.model.User
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */

// Google Sign in:
// Client ID: 768552120145-abp9v8hb05n3skksjg44jgblsnkudan7.apps.googleusercontent.com
// Client Secret: GiFS-xS6M9M3-kmY8mKgclpV

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser? by lazy { firebaseAuth.currentUser }

    private var createAccountLiveData: MutableLiveData<Int> = MutableLiveData()
    private var loginLiveData: MutableLiveData<Int> = MutableLiveData()
    private var passwordResetLiveData: MutableLiveData<Int> = MutableLiveData()

    private val user = User()

    // Creates Account
    fun createAccountWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    user.userId = firebaseUser?.uid.toString()
                    user.userEmail = firebaseUser?.email.toString()
                    user.isNewUser = true
                    createAccountLiveData.value = 1
                } else {
                    logMessage("creation ${authTask.exception}")
                    createAccountLiveData.value = 0
                }
            }
        return createAccountLiveData
    }


    // Login User
    fun loginWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            logMessage(it.exception.toString())
            if (it.isSuccessful) {
                user.userId = firebaseUser?.uid.toString()
                user.userEmail = firebaseUser?.email.toString()
                loginLiveData.value = 1
            } else loginLiveData.value = 0
        }
        return loginLiveData
    }


    // Sending Password reset email
    fun sendPasswordResetEmail(email: String): MutableLiveData<Int> {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                logMessage(it.exception.toString())
                if (it.isSuccessful) {
                    passwordResetLiveData.value = 1
                } else passwordResetLiveData.value = 0
            }
        return passwordResetLiveData
    }
}