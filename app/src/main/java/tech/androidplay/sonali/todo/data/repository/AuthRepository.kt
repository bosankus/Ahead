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
    private lateinit var createAccountMutableLiveData: MutableLiveData<Int>
    private lateinit var loginLiveData: MutableLiveData<Int>
    private lateinit var passwordResetLiveData: MutableLiveData<String>

    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private var user: User = User()

    // Creates Account
    fun createAccountWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        createAccountMutableLiveData = MutableLiveData()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                val isNewUser: Boolean = authTask.result?.additionalUserInfo?.isNewUser!!
                if (authTask.isSuccessful) {
                    user.userId = firebaseUser?.uid.toString()
                    user.userEmail = firebaseUser?.email.toString()
                    user.userName = firebaseUser?.displayName.toString()
                    user.isCreated = true
                    user.isNewUser = isNewUser
                    createAccountMutableLiveData.value = 1
                } else createAccountMutableLiveData.value = 0
            }
        return createAccountMutableLiveData
    }


    // Login User
    fun loginWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        loginLiveData = MutableLiveData()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginLiveData.value = 1
                    logMessage("Success")
                } else {
                    loginLiveData.value = 0
                    logMessage("False ${it.exception}")
                }
            }
        return loginLiveData
    }


    // Sending Password reset email
    fun sendPasswordResetEmail(email: String): MutableLiveData<String> {
        passwordResetLiveData = MutableLiveData()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    passwordResetLiveData.value = email
                } else passwordResetLiveData.value = null
            }
        return passwordResetLiveData
    }

}