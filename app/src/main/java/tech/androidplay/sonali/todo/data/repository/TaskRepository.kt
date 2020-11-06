package tech.androidplay.sonali.todo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
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


class TaskRepository @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val storageReference: StorageReference,
    private val taskListRef: CollectionReference,
) {

    private val userDetails = firebaseAuth.currentUser

    private val query: Query = taskListRef
        .whereEqualTo("id", userDetails?.uid)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)


    suspend fun createTaskWithImage(
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
            ResultData.Success(imageUrl)
        } catch (e: Exception) {
            ResultData.Failed(false.toString())
        }
    }

    suspend fun createTaskWithoutImage(
        todoBody: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String,
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

    @ExperimentalCoroutinesApi
    suspend fun fetchTasksRealtime() = callbackFlow {
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


    suspend fun updateTask(taskId: String, map: Map<String, Any>) {
        try {
            taskListRef.document(taskId)
                .update(map)
                .await()
        } catch (e: Exception) {
        }
    }

    suspend fun deleteTask(docId: String) {
        try {
            taskListRef.document(docId)
                .delete()
                .await()
        } catch (e: Exception) {
        }
    }

    suspend fun uploadImage(uri: Uri, docRefId: String): ResultData<String> {
        val ref = storageReference
            .child("${userDetails?.email}/$docRefId")
        return try {
            ref.putFile(uri).await()
            val imageUrl = ref.downloadUrl.await().toString()
            ResultData.Success(imageUrl)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }
    }

    private suspend fun uploadFile(uri: Uri, docRefId: String): String {
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
}



