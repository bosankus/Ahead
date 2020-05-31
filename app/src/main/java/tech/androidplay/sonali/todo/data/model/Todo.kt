package tech.androidplay.sonali.todo.data.model

import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */

data class Todo(
    var id: String = "",
    var todoBody: String = "",
    var todoDesc: String = "",
    var todoCreationTimeStamp: String = "",
    @JvmField var isEntered: Boolean = false,
    @JvmField var isCompleted: Boolean = false
) : Serializable