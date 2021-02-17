package tech.androidplay.sonali.todo.data.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.utils.Constants.USER_COLLECTION
import tech.androidplay.sonali.todo.utils.UIHelper
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Feb/2021
 * Email: ankush@androidplay.in
 */
class AuthRepository @Inject constructor(fireStore: FirebaseFirestore) {

    @Inject
    lateinit var messaging: FirebaseMessaging
    private val userListRef = fireStore.collection(USER_COLLECTION)

    suspend fun saveUserDetailsInFireStore(userDetails: FirebaseUser) {
        val deviceToken = messaging.token.await()
        val userId = userDetails.uid
        val userMap = hashMapOf(
            "uid" to userDetails.uid,
            "email" to userDetails.email,
            "token" to deviceToken,
            "displayName" to userDetails.displayName,
            "createdOn" to UIHelper.getCurrentTimestamp()
        )
        userListRef.document(userId).set(userMap, SetOptions.merge()).await()
    }
}