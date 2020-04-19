package tech.androidplay.sonali.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:02 PM
 */

@Entity(tableName = "todo_table")
data class TodoList(

    @PrimaryKey
    @ColumnInfo(name = "todoId")
    val todoId: String,

    @ColumnInfo(name = "todoBody")
    val todoBody: String,

    @ColumnInfo(name = "todoCreationDate")
    val todoCreationDate: String,

    @ColumnInfo(name = "todoCreationTime")
    val todoCreationTime: String
)