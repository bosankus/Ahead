package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.Helper

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

    // reference to documents in a collection
    private var taskRef: DocumentReference = firestoreDb.document("Tasks/tasId")

    private lateinit var taskLiveData: MutableLiveData<Todo>
    private lateinit var todo: Todo

    // Creates Task in Firestore
    fun createNewTask(todoBody: String, todoDesc: String): MutableLiveData<Todo> {
        taskLiveData = MutableLiveData()
        val user = hashMapOf(
            "id" to firebaseUser,
            "todoBody" to todoBody,
            "todoDesc" to todoDesc
        )

        taskListRef.add(user)
            .addOnSuccessListener {
                todo = Todo(firebaseUser, todoBody, todoDesc)
                todo.isEntered = true
                taskLiveData.postValue(todo)
            }.addOnFailureListener {
                Helper().logErrorMessage("Fail")
            }

        return taskLiveData
    }


    // Firestore Realtime Data
    fun readRealtimeTask() {
        taskListRef
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) return@addSnapshotListener
                if (querySnapshot != null) {

                    // fetches the whole document on any change
//                    val snapshotList: List<DocumentSnapshot> = querySnapshot.documents
//                    for (documentSnapshot in snapshotList) {
//                        Helper().logErrorMessage(documentSnapshot.data.toString())
//                    }

                    // fetches only the document whihc was changed
                    val documentChangeList: List<DocumentChange> = querySnapshot.documentChanges
                    for (documentChangeSnapshot in documentChangeList) {
                        Helper().logErrorMessage(documentChangeSnapshot.document.data.toString())
                    }

                } else Helper().logErrorMessage("Firestore: No Data")
            }
    }
}



