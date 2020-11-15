package tech.androidplay.sonali.todo.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Nov/2020
 * Email: ankush@androidplay.in
 */

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(task: Task)

    @Delete
    suspend fun deleteTaskItem(task: Task)

    @Query("SELECT * FROM task_items")
    fun observeAllTaskItem(): LiveData<List<Task>>


}