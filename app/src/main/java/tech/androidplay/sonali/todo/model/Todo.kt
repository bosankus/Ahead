package tech.androidplay.sonali.todo.model

import com.google.firebase.firestore.DocumentId

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */


data class Todo(
    @DocumentId
    var docId: String = "",
    var creator: String = "",
    var todoBody: String = "",
    var todoDesc: String? = "",
    var todoDate: String? = "",
    var todoCreationTimeStamp: String = "",
    @JvmField
    var isCompleted: Boolean = false,
    var taskImage: String? = "",
    var assignee: List<*>? = null,
    var priority: Int? = 0,
)