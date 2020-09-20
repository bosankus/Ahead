package tech.androidplay.sonali.todo.ui.adapter

import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 16/Sep/2020
 * Email: ankush@androidplay.in
 */
interface TodoEventListener {
    fun completeTask(todo: Todo)
}