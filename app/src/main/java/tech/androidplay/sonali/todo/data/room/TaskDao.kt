package tech.androidplay.sonali.todo.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(todo: Todo)

    @Delete
    suspend fun deleteTask(todo: Todo)

    @Query("SELECT * FROM TASK_TABLE ORDER BY todoCreationTimeStamp DESC")
    fun getTaskByCreationTime(): LiveData<List<Todo>>
}