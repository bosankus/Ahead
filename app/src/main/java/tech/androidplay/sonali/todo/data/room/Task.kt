package tech.androidplay.sonali.todo.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Nov/2020
 * Email: ankush@androidplay.in
 */

@Entity(tableName = "task_items")
data class Task(
    var id: String = "",
    @PrimaryKey(autoGenerate = false)
    var docId: String = "",
    var todoBody: String = "",
    var todoDesc: String = "",
    var todoDate: String = "",
    var todoTime: String = "",
    var todoCreationTimeStamp: String = "",
    var isCompleted: Boolean = false,
    var taskImage: String? = null
)