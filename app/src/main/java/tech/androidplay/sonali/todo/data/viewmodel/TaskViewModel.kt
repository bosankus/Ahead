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
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */
class TaskViewModel @ViewModelInject constructor(private val taskRepository: TaskRepository) :
    ViewModel() {


    fun createTask(todoName: String, todoDesc: String, todoReminder: String = "") =
        liveData {
            emit(ResultData.Loading)
            emit(taskRepository.create(todoName, todoDesc, todoReminder))
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
        todoDesc: String? = ""
    ) {
        val map: Map<String, Any> =
            if (!todoBody.isNullOrEmpty() && !todoDesc.isNullOrEmpty())
                mapOf(
                    "todoBody" to todoBody,
                    "todoDesc" to todoDesc,
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

    fun deleteTask(docId: String?) {
        docId?.let { viewModelScope.launch { taskRepository.deleteTask(docId) } }
    }

    fun uploadImage(uri: Uri?, docRefId: String) =
        uri?.let {
            liveData {
                emit(ResultData.Loading)
                emit(taskRepository.uploadImage(uri, docRefId))
            }
        }

}
