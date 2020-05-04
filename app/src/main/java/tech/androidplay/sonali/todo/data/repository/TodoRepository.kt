package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.LiveData
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.TodoDao

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:36 PM
 */

class TodoRepository(private val todoDao: TodoDao) {

    val allTodo: LiveData<List<Todo>> = todoDao.getTodoList()

    suspend fun insertTodoItem(todo: ArrayList<Todo>) {
        todoDao.insertTodoItem(todo)
    }

    suspend fun updateTodoItem(todo: ArrayList<Todo>) {
        todoDao.updateTodoItem(todo)
    }

    suspend fun deleteTodoItemById(todoId: String) {
        todoDao.deleteTodoItem(todoId)
    }
}