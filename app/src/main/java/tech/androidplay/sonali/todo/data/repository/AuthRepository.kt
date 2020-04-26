package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import tech.androidplay.sonali.todo.data.User
import tech.androidplay.sonali.todo.utils.Helper

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:52 AM
 */
class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootReference: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userReference: CollectionReference = rootReference.collection("users")
    private lateinit var authenticatedUserMutableLiveData: MutableLiveData<User>

    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    private lateinit var user: User

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    private lateinit var helper: Helper

    // Google Authentication
    fun firebaseSignInWithGoogle(account: GoogleSignInAccount): MutableLiveData<User> {
        authenticatedUserMutableLiveData = MutableLiveData()
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
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

    // Creates Password Authentication
    fun createAccountWithEmailPassword(email: String, password: String): MutableLiveData<User> {
        authenticatedUserMutableLiveData = MutableLiveData()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                userId = firebaseAuth.uid.toString()
                userEmail = firebaseAuth.currentUser?.email.toString()
                user = User(userEmail, "", userId)
                authenticatedUserMutableLiveData.postValue(user)
            }
        }
        return authenticatedUserMutableLiveData
    }


    // Email & Password Authentication
    fun firebaseSignInWithEmailPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userId = firebaseUser?.uid.toString()
                userEmail = firebaseUser?.email.toString()
                user = User(userEmail, "", userId)
            }
        }
    }

    // Creates new user record in Firestore
    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val documentReference: DocumentReference = userReference.document(authenticatedUser.userId)
        documentReference.get().addOnCompleteListener { userIdTask ->
            if (userIdTask.isSuccessful) {
                val document: DocumentSnapshot? = userIdTask.result
                if (!document!!.exists()) {
                    documentReference.set(authenticatedUser)
                        .addOnCompleteListener { userCreationTask ->
                            if (userCreationTask.isSuccessful) {
                                authenticatedUser.isCreated = true
                                newUserMutableLiveData.postValue(authenticatedUser)
                            } else {
                                helper.logErrorMessage(userCreationTask.exception?.message.toString())
                            }
                        }
                } else {
                    newUserMutableLiveData.postValue(authenticatedUser)
                }
            } else {
                helper.logErrorMessage(userIdTask.exception?.message.toString())
            }
        }
        return newUserMutableLiveData
    }

}