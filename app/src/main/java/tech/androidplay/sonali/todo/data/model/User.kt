package tech.androidplay.sonali.todo.data.model

import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/19/2020, 12:37 AM
 */
data class User(
    var userId: String? = "",
    var userEmail: String? = "",
    var userName: String? = "",
    var userDp: String? = "",
) : Serializable