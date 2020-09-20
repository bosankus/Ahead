package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    fun createTask(todoName: String, todoDesc: String) =
        liveData {
            emit(ResultData.Loading)
            emit(taskRepository.create(todoName, todoDesc))
        }

    @ExperimentalCoroutinesApi
    fun fetchRealtime(): MutableLiveData<ResultData<MutableList<Todo>>> {
        val response = MutableLiveData<ResultData<MutableList<Todo>>>()
        viewModelScope.launch {
            taskRepository.fetchTasks()
                .collect {
                    response.postValue(it)
                }
        }
        return response
    }

    fun changeTaskState(todoItem: Todo, status: Boolean) {
        viewModelScope.launch { taskRepository.changeTaskState(todoItem.docId, status) }
    }


    fun uploadImage(uri: Uri) =
        liveData {
            emit(ResultData.Loading)
            emit(taskRepository.uploadImage(uri))
        }
}
