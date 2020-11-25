package tech.androidplay.sonali.todo.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */

@ExperimentalCoroutinesApi
class FirebaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val storageReference: StorageReference,
    private val taskListRef: CollectionReference,
) : FirebaseImpl {

    private val userDetails = firebaseAuth.currentUser

    private val query: Query = taskListRef
        .whereEqualTo("id", userDetails?.uid)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)

    override suspend fun logInUser(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            ResultData.Success(response.user)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    override suspend fun createAccount(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            ResultData.Success(response.user)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    override suspend fun resetPassword(email: String) {
        try {
            firebaseAuth
                .sendPasswordResetEmail(email)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun fetchTaskRealtime(): Flow<ResultData<MutableList<Todo>>> = callbackFlow {
        offer(ResultData.Loading)
        val querySnapshot = query
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                else if (!value?.isEmpty!!) {
                    val todo = value.toObjects(Todo::class.java)
                    offer(ResultData.Success(todo))
                } else offer(ResultData.Failed())
            }
        awaitClose {
            querySnapshot.remove()
        }
    }

    override suspend fun changeTaskStatus(taskId: String, map: Map<String, Boolean>) {
        taskListRef.document(taskId)
            .update(map)
            .await()
    }

    override suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        taskListRef.document(taskId)
            .update(map)
            .await()
    }

    override suspend fun deleteTask(docId: String) {
        taskListRef.document(docId)
            .delete()
            .await()
    }

    override suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String> {
        val ref = storageReference
            .child("${userDetails?.email}/$docRefId")
        return try {
            ref.putFile(uri).await()
            val imageUrl = ref.downloadUrl.await().toString()
            val newImgMap = mapOf("taskImage" to imageUrl)
            updateTask(docRefId, newImgMap)
            ResultData.Success(imageUrl)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    override suspend fun uploadFile(uri: Uri, docRefId: String): String {
        val ref = storageReference
            .child("${userDetails?.email}/$docRefId")
        return try {
            ref.putFile(uri).await()
            val imageUrl = ref.downloadUrl.await().toString()
            imageUrl
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun createTaskWithImage(
        todoBody: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String,
        uri: Uri
    ): ResultData<String> {
        val task = hashMapOf(
            "id" to userDetails?.uid,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoTime" to todoTime,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isCompleted" to false
        )
        return try {
            // Network: Create task
            val docRef = taskListRef
                .add(task)
                .await()
            // Network: Upload Image for the task Id
            val imageUrl = uploadFile(uri, docRef.id)
            // Network: Store Image Url in the task
            updateTask(docRef.id, mapOf("taskImage" to imageUrl))
            // return image url
            ResultData.Success(docRef.id)
        } catch (e: Exception) {
            ResultData.Failed(false.toString())
        }
    }

    override suspend fun createTaskWithoutImage(
        todoBody: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String
    ): ResultData<String> {
        val task = hashMapOf(
            "id" to userDetails?.uid,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoTime" to todoTime,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isCompleted" to false
        )
        return try {
            val docRef = taskListRef
                .add(task)
                .await()
            ResultData.Success(docRef.id)
        } catch (e: Exception) {
            ResultData.Failed(false.toString())
        }
    }
}


