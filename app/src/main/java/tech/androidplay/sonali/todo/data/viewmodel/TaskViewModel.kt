package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TaskRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel() : ViewModel() {

    private val taskRepository: TaskRepository by lazy { TaskRepository() }
    lateinit var createdTaskLiveData: LiveData<Todo>

    fun createTaskInFirestore(todoName: String, todoDesc: String) {
        createdTaskLiveData = taskRepository.createNewTask(todoName, todoDesc)
    }

    fun readTaskFromFirestore() {
        viewModelScope.launch {
            taskRepository.readRealtimeTask()
        }

    }

}