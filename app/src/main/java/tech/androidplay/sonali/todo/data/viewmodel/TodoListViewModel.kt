package tech.androidplay.sonali.todo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.TodoRoomDatabase
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.Helper

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:42 PM
 */
class TodoListViewModel constructor(application: Application) : AndroidViewModel(application) {

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

    suspend fun insertTodoItem(todoList: ArrayList<Todo>) {
        viewModelScope.launch {
            todoRepository.insertTodoItem(todoList)
        }

    }

    suspend fun deleteTodoItem(todoId: String) {
        viewModelScope.launch {
            todoRepository.deleteTodoItemById(todoId)
        }
    }

    suspend fun updateTodoItem(todoList: ArrayList<Todo>) {
        viewModelScope.launch {
            todoRepository.updateTodoItem(todoList)
        }
    }

    fun userNeedDocs() {
        viewModelScope.launch {
            fetchDocs()
        }

    }

    private suspend fun fetchDocs() {
        while (true) {
            delay(1_000)
            Helper().logErrorMessage("delayed 1 Sec")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}