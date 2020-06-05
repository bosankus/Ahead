package tech.androidplay.sonali.todo.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */
class TaskRepository {

    private var userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var taskListRef: CollectionReference =
        FirebaseFirestore.getInstance().collection("Tasks")
    private val todoList = arrayListOf<Todo>()
    private var todo: Todo = Todo()

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
        taskListRef.document(taskId)
            .update("isCompleted", status)
            .addOnSuccessListener { completeTaskLiveData.value = true }
            .addOnCanceledListener { completeTaskLiveData.value = false }
        return completeTaskLiveData
    }


    fun fetchTasks(): MutableLiveData<MutableList<Todo>> {
        val fetchedTodoLiveData = MutableLiveData<MutableList<Todo>>()
        val query: Query = taskListRef
            .whereEqualTo("id", userId)
            .orderBy("todoCreationTimeStamp", Query.Direction.DESCENDING)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                logMessage(exception.message.toString())
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val snapshotList = snapshot.documents
                todoList.clear()
                snapshotList.forEach {
                    val todoItems = it.toObject(Todo::class.java)
                    todoList.add(0, todoItems!!)
                }
                fetchedTodoLiveData.value = todoList
            } else {
                logMessage("No Internet") // not finalised
            }
        }
        return fetchedTodoLiveData
    }
}



