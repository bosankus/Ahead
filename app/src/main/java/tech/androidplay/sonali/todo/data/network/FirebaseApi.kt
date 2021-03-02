package tech.androidplay.sonali.todo.data.network

import android.net.Uri
import com.google.firebase.storage.StorageReference
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
    /*suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser>
    suspend fun createAccount(
        email: String,
        password: String,
        fName: String,
        lName: String
    ): ResultData<FirebaseUser>

    suspend fun resetPassword(email: String): ResultData<String>
    suspend fun signOut()*/

    // For Firestore
    suspend fun createTask(taskMap: HashMap<*, *>): ResultData<String>
    suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchTaskByTaskId(taskId: String): ResultData<Todo>
    suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean>
    suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean>

    // For user related
    suspend fun isUserAvailable(email: String): ResultData<Boolean>

    // For Feedback
    suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String>

    // For Firebase storage
    suspend fun uploadImage(uri: Uri, docRefId: String, imgPathRef: StorageReference?):
            ResultData<String>

    // For updating new token
    /*suspend fun sendTokenToSever(token: String?)*/
}