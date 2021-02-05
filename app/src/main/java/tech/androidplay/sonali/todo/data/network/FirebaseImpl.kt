package tech.androidplay.sonali.todo.data.network

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Feb/2021
 * Email: ankush@androidplay.in
 */

abstract class FirebaseImpl : FirebaseApi {


    override suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        fName: String,
        lName: String
    ): ResultData<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun checkAssigneeAvailability(email: String): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(
        taskMap: HashMap<*, *>,
        assignee: String?,
        uri: Uri?
    ): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(docId: String, hasImage: Boolean): ResultData<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        uri: Uri,
        imgPathRef: StorageReference?,
        docRefId: String?
    ): ResultData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun sendTokenToSever(token: String?) {
        TODO("Not yet implemented")
    }
}