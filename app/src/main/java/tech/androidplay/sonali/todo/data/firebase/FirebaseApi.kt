package tech.androidplay.sonali.todo.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import tech.androidplay.sonali.todo.data.model.Todo
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
    suspend fun resetPassword(email: String)
    suspend fun signOut()

    // For Firestore
    suspend fun fetchTaskRealtime(): Flow<ResultData<MutableList<Todo>>>
    suspend fun changeTaskStatus(taskId: String, map: Map<String, Boolean>)
    suspend fun updateTask(taskId: String, map: Map<String, Any?>)
    suspend fun deleteTask(docId: String)
    suspend fun createTaskWithImage(
        todoBody: String, todoDesc: String, todoDate: String, todoTime: String, uri: Uri
    ): ResultData<String>

    suspend fun createTaskWithoutImage(
        todoBody: String, todoDesc: String, todoDate: String, todoTime: String,
    ): ResultData<String>
    suspend fun provideFeedback(topic: String, description: String): ResultData<String>

    // For Firebase storage
    suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String>
    suspend fun uploadFile(uri: Uri, docRefId: String): String
}