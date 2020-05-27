package tech.androidplay.sonali.todo.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.model.User
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.UIHelper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */
class TaskRepository {

    // accessing firestore
    private var firestoreDb = FirebaseFirestore.getInstance()
    private var userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    // reference to task collection
    private var taskListRef: CollectionReference = firestoreDb.collection("Tasks")

    private var todo: Todo = Todo()

    /**
     * Firestore Database
     * -------------------------------------------------------------------------------------
     */

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

        taskListRef.add(task)
            .addOnSuccessListener {
                todo = Todo(userId, todoBody, todoDesc)
                todo.isEntered = true
                createTaskLiveData.postValue(todo)
            }.addOnFailureListener {
                logMessage("Fail")
            }
        return createTaskLiveData
    }


    fun completeTask(taskId: String): MutableLiveData<Boolean> {
        val completeTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()
        val task = hashMapOf("isCompleted" to true)
        taskListRef.add(task)

        return completeTaskLiveData
    }


    fun fetchTasks(): MutableLiveData<MutableList<Todo>> {
        val fetchedTodoLiveData = MutableLiveData<MutableList<Todo>>()
        val todoList = mutableListOf<Todo>()
        val query: Query = taskListRef
            .whereEqualTo("id", userId)
            .orderBy("todoCreationTimeStamp", Query.Direction.DESCENDING)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                logMessage(exception.message.toString())
            }
            if (snapshot != null) {
                val snapshotList = snapshot.documents
                for (documentSnapshot in snapshotList) {
                    val todoId = documentSnapshot.id
                    val todoBody = documentSnapshot["todoBody"].toString()
                    val todoDesc = documentSnapshot["todoDesc"].toString()
                    val todoItems = Todo(todoId, todoBody, todoDesc)
                    todoList.add(0, todoItems)
                }
                fetchedTodoLiveData.value = todoList
            } else {
                logMessage("No Internet")
            }
        }
        return fetchedTodoLiveData
    }

}



