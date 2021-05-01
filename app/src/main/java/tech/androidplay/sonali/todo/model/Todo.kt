package tech.androidplay.sonali.todo.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */

/** Data class for "todo" operations on Firestore DB */

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
    @SerializedName("assignee")
    var assigneeList: List<*>? = null,
    var priority: Int? = 0,
) : Serializable