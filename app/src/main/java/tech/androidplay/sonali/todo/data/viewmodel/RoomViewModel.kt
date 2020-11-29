package tech.androidplay.sonali.todo.data.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.data.room.RoomApi
import tech.androidplay.sonali.todo.data.room.Task
import tech.androidplay.sonali.todo.utils.Event
import tech.androidplay.sonali.todo.utils.Resource

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */

class RoomViewModel @ViewModelInject constructor(private val api: RoomApi) :
    ViewModel() {

    val observeAllTasks = api.observeAllTasks()

    private val _images = MutableLiveData<Event<Resource<PixabayImageResponse>>>()
    val images: LiveData<Event<Resource<PixabayImageResponse>>>
        get() = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String>
        get() = _curImageUrl

    private fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    private val _insertTaskItemStatus = MutableLiveData<Event<Resource<Task>>>()
    val insertTaskItemStatus: LiveData<Event<Resource<Task>>>
        get() = _insertTaskItemStatus


    fun deleteTask(task: Task) = viewModelScope.launch {
        api.deleteTask(task)
    }

    private fun insertTaskToDb(task: Task) = viewModelScope.launch {
        api.insertTask(task)
    }

    fun insertTask(
        id: String, docId: String, todoBody: String, todoDesc: String,
        todoDate: String, todoTime: String, todoCreationTimeStamp: String,
        isCompleted: Boolean, taskImage: String
    ) {
        if (todoBody.isEmpty() || todoDesc.isEmpty()) {
            _insertTaskItemStatus.postValue(Event(Resource.error("Fields must not be empty", null)))
            return
        }
        val task = Task(
            id,
            docId,
            todoBody,
            todoDesc,
            todoDate,
            todoTime,
            todoCreationTimeStamp,
            isCompleted,
            taskImage
        )
        insertTaskToDb(task)
        setCurImageUrl("")
        _insertTaskItemStatus.postValue(Event(Resource.success(task)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isNotEmpty()) {
            return
        }
        _images.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = api.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}