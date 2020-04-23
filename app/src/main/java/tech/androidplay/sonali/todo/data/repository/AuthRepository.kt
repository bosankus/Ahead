package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthCredential
import tech.androidplay.sonali.todo.data.User

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */
class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authenticatedUserMutableLiveData: MutableLiveData<User>

    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private lateinit var user: User

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    // Google Authentication
    fun firebaseSignInWithGoogle(authCredential: GoogleAuthCredential): MutableLiveData<User> {
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { authTask ->
            run {
                if (authTask.isSuccessful) {
                    val isNewUser: Boolean = authTask.result?.additionalUserInfo?.isNewUser!!
                    if (firebaseUser != null) {
                        userId = firebaseUser.uid
                        userEmail = firebaseUser.email.toString()
                        userName = firebaseUser.displayName.toString()
                        user = User(userEmail, userName, userId)
                        user.isNewUser = isNewUser
                        authenticatedUserMutableLiveData.postValue(user)
                    }
                }
            }
        }
        return authenticatedUserMutableLiveData
    }


    // Email & Password Authentication
    fun firebaseSignInWithEmailPassword(email: String, password: String): MutableLiveData<User> {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userId = firebaseUser?.uid.toString()
                userEmail = firebaseUser?.email.toString()
                user = User(userEmail, "", userId)
                authenticatedUserMutableLiveData.postValue(user)
            }
        }
        return authenticatedUserMutableLiveData
    }

    // Logout User
    fun firebaseSignOut() {
        firebaseAuth.signOut()
    }

}