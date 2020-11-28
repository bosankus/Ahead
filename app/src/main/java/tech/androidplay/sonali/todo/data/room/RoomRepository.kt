package tech.androidplay.sonali.todo.data.room

import androidx.lifecycle.LiveData
import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.data.pixabay.PixabayImageApi
import tech.androidplay.sonali.todo.data.remote.PixabayAPI
import tech.androidplay.sonali.todo.utils.Resource
import javax.inject.Inject

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */

class RoomRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val pixabayAPI: PixabayAPI
) : RoomApi, PixabayImageApi {

    override suspend fun insertTask(task: Task) {
        taskDao.insertTaskItem(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTaskItem(task)
    }

    override fun observeAllTasks(): LiveData<List<Task>> {
        return taskDao.observeAllTaskItem()
    }

    override suspend fun searchForImage(query: String): Resource<PixabayImageResponse> {
        return try {
            val result = pixabayAPI.searchForImage(query)
            if (result.isSuccessful) {
                result.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Something went wrong", null)
            } else Resource.error("Something went wrong", null)
        } catch (e: Exception) {
            Resource.error("Oops! ${e.message}", null)
        }
    }
}