package tech.androidplay.sonali.todo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.data.network.FirebaseApi
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.model.User
import tech.androidplay.sonali.todo.utils.Constants.FEEDBACK_COLLECTION
import tech.androidplay.sonali.todo.utils.Constants.TASK_COLLECTION
import tech.androidplay.sonali.todo.utils.Constants.USER_COLLECTION
import tech.androidplay.sonali.todo.utils.ResultData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 10/Feb/2021
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class TodoRepository : FirebaseApi {
    val userDetails = FirebaseAuth.getInstance().currentUser
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = FirebaseStorage.getInstance().reference

    // reference to fireStore tables
    private val taskListRef = db.collection(TASK_COLLECTION)
    private val userListRef = db.collection(USER_COLLECTION)
    private val feedbackListRef = db.collection(FEEDBACK_COLLECTION)

    // query to fireStore table
    private val query: Query = taskListRef
        .whereEqualTo("creator", userDetails?.uid)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)
    private val assignedTaskQuery: Query = taskListRef
        .whereArrayContains("assignee", userDetails?.uid!!)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)


    override suspend fun createTask(taskMap: HashMap<*, *>): ResultData<String> =
        suspendCoroutine { cont ->
            taskListRef.add(taskMap)
                .addOnSuccessListener { cont.resume(ResultData.Success(it.id)) }
                .addOnFailureListener { cont.resume(ResultData.Failed(it.message)) }
        }

    override suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>> = callbackFlow {
        val querySnapshot = query.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            else value?.let { offer(it.toObjects(Todo::class.java)) }
                ?: offer(mutableListOf<Todo>())
        }
        awaitClose { querySnapshot.remove() }
    }

    override suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>> = callbackFlow {
        val querySnapshot = assignedTaskQuery.addSnapshotListener { value, error ->
            if (error != null) return@addSnapshotListener
            else value?.let { offer(it.toObjects(Todo::class.java)) }
                ?: offer(mutableListOf<Todo>())
        }
        awaitClose { querySnapshot.remove() }
    }

    override suspend fun fetchTaskByTaskId(taskId: String): ResultData<Todo> = try {
        val docSnapshot: DocumentSnapshot = taskListRef.document(taskId).get().await()
        if (docSnapshot.exists()) ResultData.Success(docSnapshot.toObject(Todo::class.java))
        else ResultData.Failed("Document do not exists")
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }

    override suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean> =
        try {
            taskListRef.document(taskId).update(map).await()
            ResultData.Success(true)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }

    override suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean> =
        try {
            taskListRef.document(docId).delete().await()
            if (!taskImageLink.isNullOrEmpty()) storage.storage.getReferenceFromUrl(taskImageLink)
                .delete().await()
            ResultData.Success(true)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }

    override suspend fun markTaskComplete(map: Map<String, Any?>, docId: String): Boolean = try {
        taskListRef.document(docId).update(map).await()
        true
    } catch (e: Exception) {
        false
    }

    override suspend fun isUserAvailable(email: String): ResultData<Boolean> = try {
        val response = userListRef.whereEqualTo("email", email).get().await()
        if (response.toObjects(User::class.java)[0].uid.isNotEmpty()) ResultData.Success(true)
        else ResultData.Success(false)
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }

    suspend fun fetchUserFullName(userId: String): String? = try {
        val response = userListRef.whereEqualTo("uid", userId).get().await()
        if (!response.isEmpty) response.toObjects(User::class.java)[0].displayName
        else "Unknown"
    } catch (e: Exception) {
        ""
    }

    override suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String> =
        try {
            val feedback = feedbackListRef.add(hashMap).await()
            ResultData.Success(feedback.id)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }

    override suspend fun uploadImage(uri: Uri, docRefId: String, imgPathRef: StorageReference?):
            ResultData<String> = try {
        val pathRef = (imgPathRef ?: storage.child("${userDetails?.email}/$docRefId"))
        pathRef.putFile(uri).await()
        val imageUrl = pathRef.downloadUrl.await().toString()
        updateTask(docRefId, mapOf("taskImage" to imageUrl))
        ResultData.Success()
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }

    // Use this for upload
    fun upload(uri: Uri, block: ((ResultData<Uri>, Int) -> Unit)?) {
        val pathRef = storage.child(("${userDetails?.email}/${uri.lastPathSegment}"))
        pathRef.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                val percentComplete = if (taskSnapshot.totalByteCount > 0) {
                    (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                } else 0
                block?.invoke(ResultData.Loading, percentComplete)
            }.continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                pathRef.downloadUrl
            }
            .addOnSuccessListener { block?.invoke(ResultData.Success(it), 100) }
            .addOnFailureListener { block?.invoke(ResultData.Failed(it.message), 0) }
    }
}