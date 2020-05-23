package tech.androidplay.sonali.todo.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Helper.getCurrentTimestamp
import tech.androidplay.sonali.todo.utils.Helper.logMessage

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */
object TaskRepository {

    // accessing registered userid
    private var firebaseUserId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    // accessing firestore
    private var firestoreDb = FirebaseFirestore.getInstance()

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
        val user = hashMapOf(
            "id" to firebaseUserId,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isEntered" to true,
            "isCompleted" to false
        )

        taskListRef.add(user)
            .addOnSuccessListener {
                todo = Todo(firebaseUserId, todoBody, todoDesc)
                todo.isEntered = true
                createTaskLiveData.postValue(todo)
            }.addOnFailureListener {
                logMessage("Fail")
            }
        return createTaskLiveData
    }


    fun fetchTasks(): MutableLiveData<MutableList<Todo>> {
        val fetchedTodoLiveData = MutableLiveData<MutableList<Todo>>()
        val todoList = mutableListOf<Todo>()
        val query: Query = taskListRef
            .whereEqualTo("id", firebaseUserId)
            .orderBy("todoCreationTimeStamp", Query.Direction.DESCENDING)

        query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                logMessage(exception.message.toString())
            }
            if (snapshot != null) {
                val snapshotList = snapshot.documents
                for (documentSnapshot in snapshotList) {
                    val todoId = documentSnapshot["id"].toString()
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



