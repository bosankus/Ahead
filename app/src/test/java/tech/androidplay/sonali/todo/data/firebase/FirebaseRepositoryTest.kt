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
class FirebaseRepositoryTest : FirebaseApi {
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

    override suspend fun createTaskWithImage(taskMap: HashMap<*, *>, uri: Uri): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun createTaskWithoutImage(taskMap: HashMap<*, *>): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTaskRealtime(): Flow<ResultData<MutableList<Todo>>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(docId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String> {
        TODO("Not yet implemented")
    }


}