package tech.androidplay.sonali.todo.data

import androidx.lifecycle.LiveData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:36 PM
 */

class TodoRepository (private val todoDao: TodoDao) {

    val allTodo: LiveData<List<TodoList>> = todoDao.getTodoList()

    suspend fun insertTodoItem(todoList: TodoList) {
        todoDao.insertTodoItem(todoList)
    }

    suspend fun deleteTodoItem(todoList: TodoList) {
        todoDao.deleteTodoItem(todoList.todoId)
    }
}