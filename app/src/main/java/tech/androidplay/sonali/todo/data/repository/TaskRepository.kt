package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Helper
import java.util.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 4:54 AM
 */
class TaskRepository {

    // accessing registered userid
    private var firebaseUser: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    // accessing firestore
    private var firestoreDb = FirebaseFirestore.getInstance()

    // reference to task collection
    private var taskListRef: CollectionReference = firestoreDb.collection("Tasks")

    // Realtime DB
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Tasks")

    // Livedata
    private val createLiveData: MutableLiveData<Todo> = MutableLiveData()
    val fetchLiveData: MutableLiveData<Todo> = MutableLiveData()

    private var todo: Todo = Todo()
    private val todoItem: MutableList<Todo> = mutableListOf()

    /**
     * Firestore Database
     * -------------------------------------------------------------------------------------
     */

    fun createNewTask(todoBody: String, todoDesc: String): MutableLiveData<Todo> {
        val user = hashMapOf(
            "id" to firebaseUser,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc
        )

        taskListRef.add(user)
            .addOnSuccessListener {
                todo = Todo(firebaseUser, todoBody, todoDesc)
                todo.isEntered = true
                createLiveData.postValue(todo)
            }.addOnFailureListener {
                Helper().logErrorMessage("Fail")
            }

        return createLiveData
    }

    fun fetchTodo() {
        taskListRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) return@addSnapshotListener
                if (querySnapshot != null) {
                    // fetches the all documents on any change
                    Helper().logErrorMessage("called")
                    val snapshotList = querySnapshot.documents
                    for (documentSnapshot in snapshotList) {
                        todoItem.clear()
                        val todoItem = documentSnapshot.toObject(Todo::class.java)
                        todoItem?.isCompleted = true
                        fetchLiveData.value = todoItem
                    }
                } else Helper().logErrorMessage("Firestore: No value")
            }
    }

    fun fetchTodoChange() {
        taskListRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) return@addSnapshotListener
                if (querySnapshot != null) {
                    val documentChange = querySnapshot.documentChanges
                    for (documentChangeSnapshot in documentChange) {
                        val todoItemChange =
                            documentChangeSnapshot.document.toObject(Todo::class.java)
                        todoItemChange.isCompleted = true
                        fetchLiveData.value = todoItemChange
                    }
                } else Helper().logErrorMessage("Firestore: No Change in Data")
            }
    }

    fun fetchTodoItem(documentId: String? = null) {
        taskListRef.document(documentId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) return@addSnapshotListener
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Helper().logErrorMessage("${documentSnapshot.data}")
                } else Helper().logErrorMessage("Firestore: No Data")
            }
    }


    /**
     * Realtime Database
     * -------------------------------------------------------------------------------------
     */

    fun createTaskRdb(todoBody: String, todoDesc: String): MutableLiveData<Todo> {
        val taskId: String = UUID.randomUUID().toString().substring(0, 8)
        todo = Todo(taskId, todoBody, todoDesc, true)
        databaseReference.child(firebaseUser).child(taskId).setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                createLiveData.postValue(todo)
            }
        }
        return createLiveData
    }
}



