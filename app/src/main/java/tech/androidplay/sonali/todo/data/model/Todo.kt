package tech.androidplay.sonali.todo.data.model

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 5:30 AM
 */

class Todo(todoId: String, todoBody: String, todoDesc: String) {

    val todoId: String by lazy { todoId }

    val todoBody: String by lazy { todoBody }

    val todoDesc: String by lazy { todoDesc }

    val isCompleted: Boolean = false

    var isEntered: Boolean = false
}