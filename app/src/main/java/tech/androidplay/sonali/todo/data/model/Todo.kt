package tech.androidplay.sonali.todo.data.model

import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */

data class Todo(
    @SerializedName("id")
    var id: String = "",
    @DocumentId
    var docId: String = "",
    @SerializedName("todoBody")
    var todoBody: String = "",
    @SerializedName("todoDesc")
    var todoDesc: String = "",
    @SerializedName("todoDate")
    var todoDate: String = "",
    @SerializedName("todoTime")
    var todoTime: String = "",
    @SerializedName("todoCreationTimeStamp")
    var todoCreationTimeStamp: String = "",
    @JvmField
    @SerializedName("isCompleted")
    var isCompleted: Boolean = false,
    @SerializedName("taskImage")
    var taskImage: String? = null
) : Serializable