package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import tech.androidplay.sonali.todo.data.Todo
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.data.TodoRoomDatabase

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:42 PM
 */
class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    private val todoRepository: TodoRepository
    private val allTodoRepository: LiveData<List<Todo>>

    init {
        val todoDao = TodoRoomDatabase.getDatabase(
            application
        ).todoDao()
        todoRepository =
            TodoRepository(todoDao)
        allTodoRepository = todoRepository.allTodo
    }

    suspend fun insertTodoItem(todoList: Todo) = todoRepository.insertTodoItem(todoList)

    suspend fun deleteTodoItem(todoId: String) = todoRepository.deleteTodoItemById(todoId)
}