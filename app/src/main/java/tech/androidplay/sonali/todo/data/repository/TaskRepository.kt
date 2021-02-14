package tech.androidplay.sonali.todo.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.model.User
import tech.androidplay.sonali.todo.utils.Constants.TASK_COLLECTION
import tech.androidplay.sonali.todo.utils.Constants.USER_COLLECTION
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Feb/2021
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
class TaskRepository @Inject constructor(
    private val storageReference: StorageReference,
    private val crashReport: FirebaseCrashlytics,
    fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    @Inject
    lateinit var messaging: FirebaseMessaging
    val userDetails = firebaseAuth.currentUser
    private val userListRef = fireStore.collection(USER_COLLECTION)
    private val taskListRef = fireStore.collection(TASK_COLLECTION)
    private val query: Query = taskListRef
        .whereEqualTo("creator", userDetails?.uid)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)

    suspend fun createTask(taskMap: HashMap<*, *>, assignee: String?, uri: Uri?):
            ResultData<String> {
        return try {
            val docRef = taskListRef.add(taskMap).await()
            uri?.let {
                when (uploadImage(uri, null, docRef.id)) {
                    is ResultData.Success -> ResultData.Success(docRef.id)
                    is ResultData.Failed -> ResultData.Failed("Task Created. Image Upload Failed. Please retry.")
                    else -> ResultData.Failed("Something went wrong while uploading Image")
                }
            } ?: ResultData.Success(docRef.id)
        } catch (e: Exception) {
            ResultData.Failed(false.toString())
        }
    }

    suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>> =
        callbackFlow {
            val querySnapshot = query
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    } else {
                        value?.let {
                            val todo: MutableList<Todo> = it.toObjects(Todo::class.java)
                            offer(todo)
                        } ?: offer(mutableListOf<Todo>())
                    }
                }
            awaitClose {
                querySnapshot.remove()
            }
        }

    suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>> =
        callbackFlow {
            val assignedTaskQuery: Query = taskListRef
                .whereArrayContains("assignee", userDetails?.uid!!)
                .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)
            val querySnapshot = assignedTaskQuery.addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                } else {
                    value?.let {
                        val todo: MutableList<Todo> = it.toObjects(Todo::class.java)
                        offer(todo)
                    } ?: offer(mutableListOf<Todo>())
                }
            }
            awaitClose {
                querySnapshot.remove()
            }
        }

    suspend fun fetchTaskByTaskId(taskId: String): ResultData<Todo> {
        val docSnapshot: DocumentSnapshot = taskListRef.document(taskId).get().await()
        return if (docSnapshot.exists()) {
            val task = docSnapshot.toObject(Todo::class.java)
            ResultData.Success(task)
        } else ResultData.Failed("Document do not exists")
    }

    suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        try {
            taskListRef.document(taskId).update(map).await()
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
        }
    }

    suspend fun deleteTask(docId: String, hasImage: Boolean): ResultData<Boolean> {
        return try {
            taskListRef.document(docId).delete().await()
            if (hasImage)
                storageReference.child("${userDetails?.email}/$docId").delete().await()
            else ResultData.Success(true)
            ResultData.Success(true)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    suspend fun uploadImage(
        uri: Uri,
        imgPathRef: StorageReference?,
        docRefId: String?
    ):
            ResultData<String> {
        val pathRef =
            imgPathRef ?: storageReference.child("${userDetails?.email}/$docRefId")
        return try {
            pathRef.putFile(uri).await()
            val imageUrl = pathRef.downloadUrl.await().toString()
            docRefId?.let {
                val newImgMap = mapOf("taskImage" to imageUrl)
                updateTask(it, newImgMap)
            }
            ResultData.Success(imageUrl)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    suspend fun checkAssigneeAvailability(email: String): ResultData<String> {
        return try {
            val query: Query = userListRef.whereEqualTo("email", email)
            val result: QuerySnapshot = query.get().await()
            val assigneeId = result.toObjects(User::class.java)[0].uid
            if (assigneeId.isNotEmpty()) ResultData.Success(assigneeId)
            else ResultData.Failed("Something went wrong")
        } catch (e: Exception) {
            ResultData.Failed()
        }
    }

    suspend fun sendTokenToSever(token: String?) {
        val generatedToken = token ?: messaging.token.await()
        val tokenMap = hashMapOf("token" to generatedToken)
        userDetails?.let { userListRef.document(it.uid).set(tokenMap, SetOptions.merge()) }
    }

    suspend fun getUserFirstName(): String? {
        val userId = userDetails?.uid
        userId?.let {
            val user = userListRef.document(it).get().await()
            return user["fname"] as String
        }
        return "User"
    }

    fun signOut() = firebaseAuth.signOut()
}