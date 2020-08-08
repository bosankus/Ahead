package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


    val todo = Todo()
    var taskStatus: Boolean = todo.isCompleted

    init {
        fetchTask()
    }

    private fun fetchTask(): MutableLiveData<MutableList<Todo>> {
        fetchedTaskLiveData = taskRepository.fetchTasks()
        return fetchedTaskLiveData
    }

    fun createTask(todoName: String, todoDesc: String) {
        createdTaskLiveData = taskRepository.createNewTask(todoName, todoDesc)
    }

    fun completeTask(taskId: String, status: Boolean): MutableLiveData<Boolean> {
        taskStatus = status
        completeTaskLiveData = taskRepository.completeTask(taskId, status)
        return completeTaskLiveData
    }

    fun uploadImage(uri: Uri) {
        taskRepository.uploadImage(uri)
    }

}