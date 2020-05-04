package tech.androidplay.sonali.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:11 PM
 */

@Dao
interface TodoDao {

    @Query("SELECT * from todo_table")
    fun getTodoList(): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(todo: ArrayList<Todo>)

    @Update
    suspend fun updateTodoItem(todo: ArrayList<Todo>)

    @Delete
    suspend fun deleteTodoItem(todoId: String)
}