package tech.androidplay.sonali.todo.data.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.firebase.FirebaseRepository
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.ResultData
import tech.androidplay.sonali.todo.utils.UIHelper.getCurrentTimestamp

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/6/2020, 1:39 AM
 */

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(private val firebaseRepository: FirebaseRepository) :
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
                    firebaseRepository.createTaskWithImage(
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
            emit(firebaseRepository.createTaskWithoutImage(todoName, todoDesc, todoDate, todoTime))
        }
    }

    fun fetchTasksRealtime(): MutableLiveData<ResultData<MutableList<Todo>>> {
        val response = MutableLiveData<ResultData<MutableList<Todo>>>()
        viewModelScope.launch {
            firebaseRepository.fetchTaskRealtime().collect {
                response.postValue(it)
            }
        }
        return response
    }

    fun changeTaskStatus(taskId: String, status: Boolean) {
        val map: Map<String, Boolean> = mapOf("isCompleted" to status)
        viewModelScope.launch { firebaseRepository.changeTaskStatus(taskId, map) }
    }

    fun updateTask(
        taskId: String,
        todoBody: String? = "",
        todoDesc: String? = "",
        todoDate: String? = "",
        todoTime: String? = "",
    ) {
        val map: Map<String, Any?> = mapOf(
            "todoBody" to todoBody,
            "todoDesc" to todoDesc,
            "todoDate" to todoDate,
            "todoTime" to todoTime,
            "updatedOn" to getCurrentTimestamp()
        )
        viewModelScope.launch { firebaseRepository.updateTask(taskId, map) }
    }

    fun uploadImage(uri: Uri?, taskId: String) = uri?.let {
        liveData {
            emit(ResultData.Loading)
            emit(firebaseRepository.uploadImage(uri, taskId))
        }
    }!!


    fun deleteTask(docId: String?) {
        docId?.let { viewModelScope.launch { firebaseRepository.deleteTask(docId) } }
    }

}

