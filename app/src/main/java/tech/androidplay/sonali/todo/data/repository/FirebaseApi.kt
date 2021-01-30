package tech.androidplay.sonali.todo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Nov/2020
 * Email: ankush@androidplay.in
 */
interface FirebaseApi {

    // For User authentication
    suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser>
    suspend fun createAccount(email: String, password: String): ResultData<FirebaseUser>
    suspend fun resetPassword(email: String): ResultData<String>
    suspend fun signOut()

    // For Firestore
    suspend fun createTask(
        taskMap: HashMap<*, *>,
        assignee: String?,
        uri: Uri?
    ): ResultData<String>

    suspend fun fetchTaskRealtime(): Flow<MutableList<Todo>>
    suspend fun updateTask(taskId: String, map: Map<String, Any?>)
    suspend fun deleteTask(docId: String, hasImage: Boolean): ResultData<Boolean>
    suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String>

    // For Firebase storage
    suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String>

    // For updating new token
    suspend fun sendTokenToSever(token: String)
}