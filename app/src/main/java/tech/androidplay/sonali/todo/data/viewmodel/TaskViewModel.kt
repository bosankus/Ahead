package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.data.repository.TaskRepository
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {


    fun createTask(
        todoName: String,
        todoDesc: String,
        todoDate: String,
        todoTime: String,
        uri: Uri?
    ): LiveData<ResultData<String>> {
        return if (uri != null)
            liveData {
                emit(ResultData.Loading)
                emit(
                    taskRepository.createTaskWithImage(
                        todoName,
                        todoDesc,
                        todoDate,
                        todoTime,
                        uri
                    )
                )
            }
        else liveData {
            emit(ResultData.Loading)
            emit(taskRepository.createTaskWithoutImage(todoName, todoDesc, todoDate, todoTime))
        }
    }

    @ExperimentalCoroutinesApi
    fun fetchTasksRealtime(): MutableLiveData<ResultData<MutableList<Todo>>> {
        val response = MutableLiveData<ResultData<MutableList<Todo>>>()
        viewModelScope.launch {
            taskRepository.fetchTasksRealtime()
                .collect {
                    response.postValue(it)
                }
        }
        return response
    }

    fun updateTask(
        taskId: String?,
        status: Boolean,
        todoBody: String? = "",
        todoDesc: String? = "",
        todoDate: String? = "",
        todoTime: String? = "",
    ) {
        val map: Map<String, Any> =
            if (!todoBody.isNullOrEmpty() && !todoDesc.isNullOrEmpty() && !todoDate.isNullOrEmpty() && !todoTime.isNullOrEmpty())
                mapOf(
                    "todoBody" to todoBody,
                    "todoDesc" to todoDesc,
                    "todoDate" to todoDate,
                    "todoTime" to todoTime,
                    "updatedOn" to getCurrentTimestamp()
                )
            else mapOf(
                "isCompleted" to status,
                "updatedOn" to getCurrentTimestamp()
            )
        taskId?.let {
            viewModelScope.launch {
                taskRepository.updateTask(taskId, map)
            }
        }
    }

    fun uploadImage(uri: Uri?, taskId: String) = uri?.let {
        liveData {
            emit(ResultData.Loading)
            emit(taskRepository.uploadImage(uri, taskId))
        }
    }!!


    fun deleteTask(docId: String?) {
        docId?.let { viewModelScope.launch { taskRepository.deleteTask(docId) } }
    }

}

