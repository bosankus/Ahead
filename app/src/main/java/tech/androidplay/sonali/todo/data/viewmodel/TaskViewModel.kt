package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.utils.ResultData

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {

    var completeTaskLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun createTask(todoName: String, todoDesc: String) =
        liveData {
            emit(ResultData.Loading)
            emit(taskRepository.create(todoName, todoDesc))
        }


    // Asynchronous
    @ExperimentalCoroutinesApi
    fun fetchRealtime(): MutableLiveData<ResultData<MutableList<Todo>>> {
        val abc = MutableLiveData<ResultData<MutableList<Todo>>>()
        viewModelScope.launch {
            taskRepository.fetchTasks()
                .collect {
                    abc.postValue(it)
                }
        }
        return abc
    }

/*fun completeTask(taskId: String, status: Boolean) {
    *//*taskStatus = status
    completeTaskLiveData = taskRepository.completeTask(taskId, status)
    return completeTaskLiveData*//*
}*/

    fun uploadImage(uri: Uri) {
        taskRepository.uploadImage(uri)
    }


    /*fun fetchTask() =
        taskRepository
            .fetchTasks()
            .catch { e -> ResultData.Failed(e.message) }
            .asLiveData(
                Dispatchers.Default +
                        viewModelScope.coroutineContext
            )*/
}
