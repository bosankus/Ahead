package tech.androidplay.sonali.todo.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
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

    private var firestoreDb = FirebaseFirestore.getInstance()
    private var userId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var taskListRef: CollectionReference = firestoreDb.collection("Tasks")
    private var taskStatusLiveData: MutableLiveData<Todo> = MutableLiveData()
    private val todoList = mutableListOf<Todo>()
    private var todo: Todo = Todo()
    var fetchedTodoLiveData = MutableLiveData<MutableList<Todo>>()

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

        taskListRef.add(task).addOnSuccessListener {
            todo = Todo(userId, todoBody, todoDesc)
            todo.isEntered = true
            createTaskLiveData.postValue(todo)
        }.addOnFailureListener {
            logMessage("Task creation failed: ${it.message}")
        }
        return createTaskLiveData
    }


    fun fetchTasks(): MutableLiveData<MutableList<Todo>> {
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
                    val todoItems =
                        Todo(
                            documentSnapshot.id,
                            documentSnapshot["todoBody"].toString(),
                            documentSnapshot["todoDesc"].toString(),
                            documentSnapshot["todoCreationTimeStamp"].toString(),
                            documentSnapshot["isEntered"] as Boolean,
                            documentSnapshot["isCompleted"] as Boolean
                        )
                    todoList.add(0, todoItems)
                }
                fetchedTodoLiveData.value = todoList
            } else {
                logMessage("No Internet")
            }
        }
        return fetchedTodoLiveData
    }


    fun fetchTaskStatus(): MutableLiveData<Todo> {
        taskListRef
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    logMessage(exception.message.toString())
                }
                if (querySnapshot != null) {
                    for (documentChangeSnapshot in querySnapshot.documentChanges) {
                        val todoItemChange =
                            documentChangeSnapshot.document
                        todoList.clear()
                        if (documentChangeSnapshot.type == DocumentChange.Type.MODIFIED) {
                            val todoItems =
                                Todo(
                                    todoItemChange.id,
                                    todoItemChange["todoBody"].toString(),
                                    todoItemChange["todoDesc"].toString(),
                                    todoItemChange["todoCreationTimeStamp"] as String,
                                    todoItemChange["isEntered"] as Boolean,
                                    todoItemChange["isCompleted"] as Boolean
                                )
                            todoList.add(todoItems)
                            taskStatusLiveData.value = todoItems
                        }
                    }
                }
            }
        return taskStatusLiveData
    }

}



