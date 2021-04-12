package tech.androidplay.sonali.todo.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.utils.ResultData
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 31/Dec/2020
 * Email: ankush@androidplay.in
 */

@SuppressLint("StaticFieldLeak")
@ExperimentalCoroutinesApi
@HiltViewModel
class EditTaskViewModel @Inject constructor(private val taskSource: TodoRepository)
    : ViewModel() {

    private var _viewState = MutableLiveData<ResultData<*>>(ResultData.Loading)
    val viewState: LiveData<ResultData<*>> get() = _viewState

    private var _imageUploadState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val imageUploadState: LiveData<ResultData<*>> get() = _imageUploadState

    private var _deleteTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val deleteTaskState: LiveData<ResultData<*>> get() = _deleteTaskState

    private var _updateTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val updateTaskState: LiveData<ResultData<*>> get() = _updateTaskState

    var initialTaskId = ObservableField("")
    var initialTaskBody = ObservableField("")
    var initialTaskDesc = ObservableField("")
    var initialTaskDate = ObservableField("")
    var initialTaskImage = ObservableField("")
    var initialTaskPriority = ObservableField<Int>()
    var initialTaskStatus = ObservableField(false)


    fun getTaskByTaskId(taskId: String?) {
        viewModelScope.launch {
            val response = taskId?.let { taskSource.fetchTaskByTaskId(it) }
            response?.let {
                initialTaskId.set(it.docId)
                initialTaskBody.set(it.todoBody.toString())
                initialTaskDesc.set(it.todoDesc.toString())
                initialTaskDate.set(it.todoDate)
                initialTaskImage.set(it.taskImage.toString())
                initialTaskPriority.set(it.priority)
                initialTaskStatus.set(it.isCompleted)
                _viewState.postValue(ResultData.Success(it))
            } ?: run { _viewState.postValue(ResultData.Failed("Check your network!")) }
        }
    }


    fun updateTask() {
        if (checkInputs()) {
            _updateTaskState.postValue(ResultData.Loading)
            val taskId = checkNotNull(initialTaskId.get())
            val taskBody = checkNotNull(initialTaskBody.get())
            val taskDesc = checkNotNull(initialTaskDesc.get())
            val taskDate = checkNotNull(initialTaskDate.get())
            val map: Map<String, Any?> = mapOf(
                "todoBody" to taskBody,
                "todoDesc" to taskDesc,
                "todoDate" to taskDate,
            )
            viewModelScope.launch {
                val response = taskSource.updateTask(taskId, map)
                response.let { _updateTaskState.postValue(ResultData.Success(it)) }
            }
        } else _updateTaskState.postValue(ResultData.Failed("Fields can not be empty"))
    }


    fun changeTaskStatus(status: Boolean) = viewModelScope.launch {
        val statusMap = mapOf("isCompleted" to status)
        initialTaskId.get()?.let { taskSource.markTaskComplete(statusMap, it) }
    }


    fun uploadImage(uri: Uri?, taskId: String) = viewModelScope.launch {
        _imageUploadState.postValue(ResultData.Loading)
        val response = uri?.let { taskSource.uploadImage(it, taskId, null) }
        response?.let { _imageUploadState.postValue(ResultData.Success(it)) }
            ?: run { _imageUploadState.postValue(ResultData.Failed()) }
    }


    fun deleteTask() = viewModelScope.launch {
        _deleteTaskState.postValue(ResultData.Loading)
        val response =
            initialTaskId.get()?.let { taskSource.deleteTask(it, initialTaskImage.get()) }
        response?.let { _deleteTaskState.postValue(ResultData.Success(it)) }
            ?: run { _deleteTaskState.postValue(ResultData.Failed("Something went wrong!")) }
    }


    private fun checkInputs(): Boolean {
        return !(initialTaskId.get().isNullOrEmpty() || initialTaskDesc.get().isNullOrEmpty() ||
                initialTaskDate.get().isNullOrEmpty())
    }

}