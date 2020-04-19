package tech.androidplay.sonali.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:11 PM
 */

@Dao
interface TodoDao {

    @Query("SELECT * from todo_table")
    fun getTodoList(): LiveData<List<TodoList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(todoList: TodoList)

    @Delete
    suspend fun deleteTodoItem(todoId: String)
}