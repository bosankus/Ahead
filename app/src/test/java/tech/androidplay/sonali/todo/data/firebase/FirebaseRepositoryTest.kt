package tech.androidplay.sonali.todo.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */
class FirebaseRepositoryTest : FirebaseImpl {

    override suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(email: String, password: String): ResultData<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTaskRealtime(): Flow<ResultData<MutableList<Todo>>> {
        TODO("Not yet implemented")
    }

    override suspend fun changeTaskStatus(taskId: String, map: Map<String, Boolean>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(docId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTaskWithImage(
        todoBody: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String,
        uri: Uri
    ): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun createTaskWithoutImage(
        todoBody: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String
    ): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadFile(uri: Uri, docRefId: String): String {
        TODO("Not yet implemented")
    }

}