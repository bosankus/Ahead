package tech.androidplay.sonali.todo.data.repository

import android.annotation.SuppressLint
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.model.User
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */

// Glide not loading dp . Upoload working with download url fetch


class TaskRepository() {

    private var userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    private var taskListRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("Tasks")

    private val query: Query = taskListRef
        .whereEqualTo("id", userId)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)

    private var storageReference = FirebaseStorage.getInstance().reference

    private val todoList = arrayListOf<Todo>()
    private var todo: Todo = Todo()
    private var user: User = User()

    @SuppressLint("SimpleDateFormat")
    fun createNewTask(todoBody: String, todoDesc: String): MutableLiveData<Todo> {
        val createTaskLiveData: MutableLiveData<Todo> = MutableLiveData()
        val task = hashMapOf(
            "id" to userId,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isEntered" to true,
            "isCompleted" to false
        )

        taskListRef.add(task).addOnSuccessListener {
            todo = Todo(userId, todoBody, todoDesc)
            todo.isEntered = true
            createTaskLiveData.postValue(todo)
        }.addOnFailureListener {
            logMessage("Task creation failed: ${it.message}")
        }
        return createTaskLiveData
    }

    fun completeTask(taskId: String, status: Boolean): MutableLiveData<Boolean> {
        val completeTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()

        GlobalScope.launch(Dispatchers.IO) {
            taskListRef.document(taskId)
                .update("isCompleted", status)
                .await()
            completeTaskLiveData.postValue(true)
        }
        return completeTaskLiveData
    }

    fun fetchTasks(): MutableLiveData<MutableList<Todo>> {
        val fetchedTodoLiveData = MutableLiveData<MutableList<Todo>>()
        GlobalScope.launch(Dispatchers.IO) {
            val result = query.get().await().documents
            if (result.isNotEmpty()) {
                todoList.clear()
                result.forEach {
                    val todoItems = it.toObject(Todo::class.java)
                    todoList.add(0, todoItems!!)
                }
                fetchedTodoLiveData.postValue(todoList)
            }
        }
        return fetchedTodoLiveData
    }


    fun uploadImage(uri: Uri) {
        val ref = storageReference.child("profilePicture/$userId")
        val uploadTask = ref.putFile(uri)
        uploadTask.addOnCompleteListener {
            if (it.isSuccessful) {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    logMessage("Repo: $uri")
                    user.userDp = uri.toString()
                    // TODO: Store the URI in shared preference
                }
            }
        }
    }
}



