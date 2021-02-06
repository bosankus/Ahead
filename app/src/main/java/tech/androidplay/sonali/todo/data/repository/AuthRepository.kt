package tech.androidplay.sonali.todo.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.Constants
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Feb/2021
 * Email: ankush@androidplay.in
 */
class AuthRepository @Inject constructor(
    private val crashReport: FirebaseCrashlytics,
    private val firebaseAuth: FirebaseAuth,
    fireStore: FirebaseFirestore
) {

    @Inject
    lateinit var messaging: FirebaseMessaging
    private val userListRef = fireStore.collection(Constants.USER_COLLECTION)

    suspend fun createAccount(
        email: String,
        password: String,
        fName: String,
        lName: String
    ): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val deviceToken = messaging.token.await()
            val userMap = hashMapOf(
                "uid" to response.user?.uid,
                "email" to response.user?.email,
                "token" to deviceToken,
                "fname" to fName,
                "lname" to lName,
                "createdOn" to UIHelper.getCurrentTimestamp()
            )
            response?.user?.let {
                userListRef.document(it.uid).set(userMap, SetOptions.merge()).await()
            }
            ResultData.Success(response.user)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            ResultData.Success(response.user)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    suspend fun resetPassword(email: String): ResultData<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResultData.Success("Reset link is sent to your mail ID")
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message.toString())
        }
    }
}