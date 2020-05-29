package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    lateinit var createdTaskLiveData: LiveData<Todo>
    var fetchedTaskLiveData: MutableLiveData<MutableList<Todo>> = MutableLiveData()
    var fetchTaskStatusLiveData: MutableLiveData<Todo> = MutableLiveData()

    fun createTask(todoName: String, todoDesc: String) {
        viewModelScope.launch {
            createdTaskLiveData = taskRepository.createNewTask(todoName, todoDesc)
        }
    }

    fun fetchTask(): MutableLiveData<MutableList<Todo>> {
        fetchedTaskLiveData = taskRepository.fetchTasks()
        return fetchedTaskLiveData
    }

    fun fetchTaskStatus(): MutableLiveData<Todo> {
        fetchTaskStatusLiveData = taskRepository.fetchTaskStatus()
        return fetchTaskStatusLiveData
    }

}