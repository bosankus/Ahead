package tech.androidplay.sonali.todo.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TaskRepository

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    var createdTaskLiveData: MutableLiveData<Todo> = MutableLiveData()
    var completeTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var fetchedTaskLiveData: MutableLiveData<MutableList<Todo>> = MutableLiveData()

    fun uploadImage(userId: String, key: String, uri: String) {
        viewModelScope.launch(Dispatchers.Default) {
            taskRepository.uploadImage(userId, key, uri)
        }
    }

    fun createTask(todoName: String, todoDesc: String) {
        createdTaskLiveData = taskRepository.createNewTask(todoName, todoDesc)
    }

    fun completeTask(taskId: String, status: Boolean): MutableLiveData<Boolean> {
        completeTaskLiveData = taskRepository.completeTask(taskId, status)
        return completeTaskLiveData
    }

    fun fetchTask(): MutableLiveData<MutableList<Todo>> {
        fetchedTaskLiveData = taskRepository.fetchTasks()
        return fetchedTaskLiveData
    }

}