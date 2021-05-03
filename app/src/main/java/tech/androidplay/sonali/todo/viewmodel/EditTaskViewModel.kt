package tech.androidplay.sonali.todo.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    application: Application,
    private val taskSource: TodoRepository
) : AndroidViewModel(application), Observable {

    private val registry = PropertyChangeRegistry()

    private var _viewState = MutableLiveData<ResultData<*>>(ResultData.Loading)
    val viewState: LiveData<ResultData<*>> get() = _viewState

    private var _taskById = MutableLiveData<Todo>()
    val taskById get(): LiveData<Todo> = _taskById

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

    var initialTaskId = ObservableField("")
    var initialTaskBody = ObservableField("")
    var initialTaskDesc = ObservableField("")
    var initialTaskDate = ObservableField("")
    var initialTaskImage = ObservableField("")
    var initialTaskStatus = ObservableField(false)


    fun getTaskByTaskId(taskId: String?) {
        viewModelScope.launch {
            val response = taskId?.let { taskSource.fetchTaskByTaskId(it) }
            if (response != null) todo = response

            response?.let {
                _taskById.value = it
                initialTaskId.set(it.docId)
                initialTaskBody.set(it.todoBody)
                initialTaskDesc.set(it.todoDesc.toString())
                initialTaskDate.set(it.todoDate)
                initialTaskImage.set(it.taskImage.toString())
                initialTaskStatus.set(it.isCompleted)
                _viewState.postValue(ResultData.Success(it))
            } ?: run { _viewState.postValue(ResultData.Failed("Check your network!")) }
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
                    )
                    viewModelScope.launch {
                        val response = taskSource.updateTask(taskItem.docId, taskMap)
                        response.let { _updateTaskState.postValue(ResultData.Success(it)) }
                    }
                }
            }
        }
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


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}