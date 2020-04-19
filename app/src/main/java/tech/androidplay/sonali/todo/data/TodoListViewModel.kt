package tech.androidplay.sonali.todo.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:42 PM
 */
class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    private val todoRepository: TodoRepository
    private val allTodoRepository: LiveData<List<TodoList>>

    init {
        val todoDao = TodoRoomDatabase.getDatabase(application).todoDao()
        todoRepository = TodoRepository(todoDao)
        allTodoRepository = todoRepository.allTodo
    }



    fun insertTodoItem(todoList: TodoList) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.insertTodoItem(todoList)
    }

    fun deleteTodoItem(todoList: TodoList) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.deleteTodoItem(todoList)
    }
}