package tech.androidplay.sonali.todo.data.network

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.model.User
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 24/Nov/2020
 * Email: ankush@androidplay.in
 */


interface FirebaseApi {

    // For storing user data
    suspend fun saveUser(user: FirebaseUser)

    // For Firestore
    suspend fun createTask(taskItem: Todo): ResultData<Todo>
    suspend fun createTaskUsingWorker(taskMap: HashMap<*, *>): String
    suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchTaskByTaskId(taskId: String): Todo?
    suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean>
    suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean>
    suspend fun markTaskComplete(map: Map<String, Any?>, docId: String): Boolean

    // For user related
    suspend fun isUserAvailable(email: String): ResultData<String>
    suspend fun fetchTaskCreatorDetails(userId: String): User?

    // For Feedback
    suspend fun provideFeedback(topic: String, description: String): ResultData<String>

    // For Firebase storage
    suspend fun uploadImage(uri: Uri, docRefId: String, imgPathRef: StorageReference?):
            ResultData<String>
    fun uploadImageFromWorker(uri: Uri, block: ((ResultData<Uri>, Int) -> Unit)?)
}