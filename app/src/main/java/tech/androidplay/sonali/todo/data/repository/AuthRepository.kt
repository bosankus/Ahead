package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import tech.androidplay.sonali.todo.data.model.User
import tech.androidplay.sonali.todo.utils.Helper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */
class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var authLiveData: MutableLiveData<Int> = MutableLiveData()

    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private var user: User = User()

    // Creates Account
    fun createAccountWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                val isNewUser: Boolean = authTask.result?.additionalUserInfo?.isNewUser!!
                if (!isNewUser) {
                    authLiveData.value = 2
                } else if (authTask.isSuccessful) {
                    authLiveData.value = 1
                } else authLiveData.value = 0
            }
        return authLiveData
    }


    // Login User
    fun loginWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            logMessage(it.exception.toString())
            if (it.isSuccessful) {
                authLiveData.value = 1
            } else authLiveData.value = 0
        }
        return authLiveData
    }


    // Sending Password reset email
    fun sendPasswordResetEmail(email: String): MutableLiveData<Int> {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authLiveData.value = 1
                } else authLiveData.value = 0
            }
        return authLiveData
    }
}