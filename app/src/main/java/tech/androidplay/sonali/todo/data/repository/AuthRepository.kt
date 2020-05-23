package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import tech.androidplay.sonali.todo.data.model.User

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */
class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private var user: User = User()

    val authLiveData: MutableLiveData<Int> = MutableLiveData()


    // Creates Account
    fun createWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                val isNewUser: Boolean = authTask.result?.additionalUserInfo?.isNewUser!!
                if (authTask.isSuccessful) {
                    user.userId = firebaseUser?.uid.toString()
                    user.userEmail = firebaseUser?.email.toString()
                    user.userName = firebaseUser?.displayName.toString()
                    user.isCreated = true
                    user.isNewUser = isNewUser
                    authLiveData.value = 1
                } else authLiveData.value = 0
            }
        return authLiveData
    }


    // Login User
    fun loginWithEmailPassword(email: String, password: String): MutableLiveData<Int> {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authLiveData.value = 1
                } else {
                    authLiveData.value = 0
                }
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