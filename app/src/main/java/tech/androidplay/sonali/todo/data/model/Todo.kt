package tech.androidplay.sonali.todo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.gson.annotations.SerializedName
import tech.androidplay.sonali.todo.utils.Constants.TASK_TABLE
import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */

@Entity(tableName = TASK_TABLE)
data class Todo(
    @PrimaryKey
    @SerializedName("id")
    var id: String = "",
    @DocumentId
    var docId: String = "",
    @SerializedName("todoBody")
    var todoBody: String = "",
    @SerializedName("todoDesc")
    var todoDesc: String = "",
    @SerializedName("todoReminder")
    var todoReminder: String = "",
    @SerializedName("todoCreationTimeStamp")
    var todoCreationTimeStamp: String = "",
    @JvmField
    @SerializedName("isCompleted")
    var isCompleted: Boolean = true
) : Serializable