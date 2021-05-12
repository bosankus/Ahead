package tech.androidplay.sonali.todo.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.BR
import tech.androidplay.sonali.todo.data.repository.TodoRepository
import tech.androidplay.sonali.todo.model.Todo
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
class EditTaskViewModel @Inject constructor(
    private val taskSource: TodoRepository
) : ViewModel(), Observable {

    private val registry = PropertyChangeRegistry()

    private var _viewState = MutableLiveData<ResultData<*>>(ResultData.Loading)
    val viewState: LiveData<ResultData<*>> get() = _viewState

    private var _imageUploadState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val imageUploadState: LiveData<ResultData<*>> get() = _imageUploadState

    private var _deleteTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val deleteTaskState: LiveData<ResultData<*>> get() = _deleteTaskState

    private var _updateTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val updateTaskState: LiveData<ResultData<*>> get() = _updateTaskState

    @get: Bindable
    var todo = Todo()
        set(value) {
            if (value != field) field = value
            registry.notifyChange(this, BR.todo)
        }


    fun getTaskByTaskId(taskId: String?) {
        viewModelScope.launch {
            val response = taskId?.let { taskSource.fetchTaskByTaskId(it) }
            if (response != null) {
                todo = response
                _viewState.postValue(ResultData.Success(response))
            } else _viewState.postValue(ResultData.Failed("Check your network!"))
        }
    }


    fun updateTask(item: Todo?) {
        item?.let { taskItem ->
            _updateTaskState.postValue(ResultData.Loading)
            when {
                taskItem.todoBody.isEmpty() ->
                    _updateTaskState.postValue(ResultData.Failed("Body can't be empty"))
                taskItem.todoDate.isNullOrEmpty() ->
                    _updateTaskState.postValue(ResultData.Failed("Date & Time can't be empty"))
                else -> {
                    val taskMap: Map<String, Any?> = mapOf(
                        "todoBody" to taskItem.todoBody,
                        "todoDesc" to taskItem.todoDesc,
                        "todoDate" to taskItem.todoDate,
                        "priority" to taskItem.priority
                    )
                    viewModelScope.launch {
                        val response = taskSource.updateTask(taskItem.docId, taskMap)
                        response.let { _updateTaskState.postValue(ResultData.Success(it)) }
                    }
                }
            }
        }
    }


    fun changeTaskStatus() = viewModelScope.launch {
        val map =
            if (todo.isCompleted) mapOf("isCompleted" to false)
            else mapOf("isCompleted" to true)
        todo.docId.let { taskSource.markTaskComplete(map, it) }
    }


    fun uploadImage(uri: Uri?, taskId: String) = viewModelScope.launch {
        _imageUploadState.postValue(ResultData.Loading)
        val response = uri?.let { taskSource.uploadImage(it, taskId, null) }
        response?.let { _imageUploadState.postValue(ResultData.Success(it)) }
            ?: run { _imageUploadState.postValue(ResultData.Failed()) }
    }


    fun deleteTask() = viewModelScope.launch {
        _deleteTaskState.postValue(ResultData.Loading)
        val response = todo.docId.let { taskSource.deleteTask(it, todo.taskImage) }
        if (response is ResultData.Success && response.data == true)
            _deleteTaskState.postValue(ResultData.Success(true))
        else _deleteTaskState.postValue(ResultData.Failed("Unable to delete. Please retry."))
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}