package tech.androidplay.sonali.todo.data.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.data.pixabay.PixabayImageApi
import tech.androidplay.sonali.todo.utils.Resource

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */
class FakeRoomRepository : RoomApi, PixabayImageApi {

    private val taskItem = mutableListOf<Task>()
    private val observableTaskItem = MutableLiveData<List<Task>>(taskItem)

    private var shouldReturnNetworkError = false

    private fun refreshTaskLiveData() {
        observableTaskItem.postValue(taskItem)
    }

//    fun setShouldReturnNetworkError(value: Boolean) {
//        shouldReturnNetworkError = value
//    }

    override suspend fun insertTask(task: Task) {
        taskItem.add(task)
        refreshTaskLiveData()
    }

    override suspend fun deleteTask(task: Task) {
        taskItem.remove(task)
        refreshTaskLiveData()
    }

    override fun observeAllTasks(): LiveData<List<Task>> {
        return observableTaskItem
    }

    override suspend fun searchForImage(query: String): Resource<PixabayImageResponse> {
        return if (shouldReturnNetworkError)
            Resource.error("Error", null)
        else Resource.success(PixabayImageResponse(listOf(), 0, 0))
    }

}