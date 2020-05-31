package tech.androidplay.sonali.todo.data.viewmodel

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
    var fetchedTaskLiveData: MutableLiveData<MutableList<Todo>> = MutableLiveData()

    fun createTask(todoName: String, todoDesc: String) {
        createdTaskLiveData = taskRepository.createNewTask(todoName, todoDesc)
    }

    fun fetchTask(): MutableLiveData<MutableList<Todo>> {
        fetchedTaskLiveData = taskRepository.fetchTasks()
        return fetchedTaskLiveData
    }
}