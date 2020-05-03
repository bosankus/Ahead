package tech.androidplay.sonali.todo.data.model

import java.io.Serializable

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/22/2020, 5:40 AM
 */
class User(userEmail: String, userName: String, userId: String) : Serializable {

    private val userEmail: String by lazy { userEmail }

    val userName: String by lazy { userName }

    val userId: String by lazy { userId }

    val isAuthenticated by lazy { }

    var isNewUser = false

    var isCreated: Boolean = false

}