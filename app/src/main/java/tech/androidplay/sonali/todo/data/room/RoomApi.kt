package tech.androidplay.sonali.todo.data.room

import androidx.lifecycle.LiveData
import tech.androidplay.sonali.todo.data.model.PixabayImageResponse
import tech.androidplay.sonali.todo.utils.Resource

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 25/Nov/2020
 * Email: ankush@androidplay.in
 */
interface RoomApi {

    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
    fun observeAllTasks(): LiveData<List<Task>>
    suspend fun searchForImage(query: String): Resource<PixabayImageResponse>
}