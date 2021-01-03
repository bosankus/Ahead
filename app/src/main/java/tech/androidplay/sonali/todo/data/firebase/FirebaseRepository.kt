package tech.androidplay.sonali.todo.data.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Constants.FEEDBACK_COLLECTION
import tech.androidplay.sonali.todo.utils.Constants.TASK_COLLECTION
import tech.androidplay.sonali.todo.utils.Constants.USER_COLLECTION
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
class FirebaseRepository @Inject constructor(
    private val crashReport: FirebaseCrashlytics,
    private val firebaseAuth: FirebaseAuth,
    private val storageReference: StorageReference,
    fireStore: FirebaseFirestore
) : FirebaseApi {

    private val userDetails = firebaseAuth.currentUser
    private val taskListRef = fireStore.collection(TASK_COLLECTION)
    private val userListRef = fireStore.collection(USER_COLLECTION)
    private val feedbackReference = fireStore.collection(FEEDBACK_COLLECTION)

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
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    override suspend fun createAccount(email: String, password: String): ResultData<FirebaseUser> {
        return try {
            val response = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()
            val userMap = hashMapOf(
                "uid" to response.user?.uid,
                "email" to response.user?.email,
                "createdOn" to getCurrentTimestamp()
            )
            response?.user?.let {
                userListRef.document("${it.email}").set(userMap, SetOptions.merge()).await()
            }
            ResultData.Success(response.user)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }

    override suspend fun resetPassword(email: String): ResultData<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            ResultData.Success("Reset link is sent to your mail ID")
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message.toString())
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun createTaskWithImage(taskMap: HashMap<*, *>, uri: Uri): ResultData<String> {
        return try {
            val docRef = taskListRef
                .add(taskMap)
                .await()
            when (uploadImage(uri, docRef.id)) {
                is ResultData.Success -> ResultData.Success(docRef.id)
                is ResultData.Failed -> ResultData.Failed("Task Created. Image Upload Failed. Please retry.")
                else -> ResultData.Failed("Something went wrong while uploading Image")
            }
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(false.toString())
        }
    }

    override suspend fun createTaskWithoutImage(taskMap: HashMap<*, *>): ResultData<String> {
        return try {
            val docRef = taskListRef
                .add(taskMap)
                .await()
            ResultData.Success(docRef.id)
        } catch (e: Exception) {
            ResultData.Failed(false.toString())
        }
    }

    override suspend fun fetchTaskRealtime(): Flow<MutableList<Todo>> =
        callbackFlow {
            val querySnapshot = query
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        crashReport.log(error.message.toString())
                        return@addSnapshotListener
                    } else {
                        val todo: MutableList<Todo> = value!!.toObjects(Todo::class.java)
                        offer(todo)
                    }
                }
            awaitClose {
                querySnapshot.remove()
            }
        }


    override suspend fun updateTask(taskId: String, map: Map<String, Any?>) {
        try {
            taskListRef.document(taskId)
                .update(map)
                .await()
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
        }
    }

    override suspend fun deleteTask(docId: String, hasImage: Boolean): ResultData<Boolean> {
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

    override suspend fun provideFeedback(hashMap: HashMap<String, String?>): ResultData<String> {
        return try {
            val feedback = feedbackReference
                .add(hashMap)
                .await()
            ResultData.Success(feedback.id)
        } catch (e: Exception) {
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
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
            crashReport.log(e.message.toString())
            ResultData.Failed(e.message)
        }
    }
}



