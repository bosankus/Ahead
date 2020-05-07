package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
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

    private lateinit var taskLiveData: MutableLiveData<Todo>
    private lateinit var todo: Todo

    /**
     * Creates Task in Firestore
     * @param todoBody : taks body
     * @param todoDesc : task description
     * @return MutableLiveData, for lifecycle observation
     */
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


    /**
     * Firestore Realtime Data
     * @param realtimeUpdateType : 1, 2, 3 : Query types
     * @param documentId : for query type 3, to fetch respective document.
     * @see link: https://www.youtube.com/watch?v=csKzASAO6p0 (Tutorial)
     */
    fun readRealtimeTask(
        realtimeUpdateType: Int? = 3,
        documentId: String? = "aG3G5diXTswVDbMX13Jp"
    ) {
        if (realtimeUpdateType == 1 || realtimeUpdateType == 2) {
            taskListRef
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) return@addSnapshotListener
                    if (querySnapshot != null) {

                        if (realtimeUpdateType == 1) {
                            // fetches the whole document on any change
                            val snapshotList: List<DocumentSnapshot> = querySnapshot.documents
                            for (documentSnapshot in snapshotList) {
                                Helper().logErrorMessage(documentSnapshot.data.toString())
                            }
                        } else if (realtimeUpdateType == 2) {
                            // fetches only the changed document
                            val documentChangeList: List<DocumentChange> =
                                querySnapshot.documentChanges
                            for (documentChangeSnapshot in documentChangeList) {
                                Helper().logErrorMessage(documentChangeSnapshot.document.data.toString())
                            }
                        }
                    } else Helper().logErrorMessage("Firestore: No Data")
                }
        } else if (realtimeUpdateType == 3 && !documentId.isNullOrEmpty()) {
            // fetches only changes inside specified documentId
            taskListRef.document(documentId!!)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) return@addSnapshotListener
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Helper().logErrorMessage("${documentSnapshot.data}")
                    } else Helper().logErrorMessage("Firestore: No Data")
                }
        }
    }
}


