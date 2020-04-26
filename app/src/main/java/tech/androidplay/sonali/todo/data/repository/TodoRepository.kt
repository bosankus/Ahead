package tech.androidplay.sonali.todo.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.Todo
import tech.androidplay.sonali.todo.data.TodoDao

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:36 PM
 */

class TodoRepository(private val todoDao: TodoDao) {

    val allTodo: LiveData<List<Todo>> = todoDao.getTodoList()

    suspend fun insertTodoItem(todo: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.insertTodoItem(todo)
        }
    }

    suspend fun updateTodoItem(todo: Todo) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.updateTodoItem(todo)
        }
    }

    suspend fun deleteTodoItemById(todoId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            todoDao.deleteTodoItem(todoId)
        }
    }
}