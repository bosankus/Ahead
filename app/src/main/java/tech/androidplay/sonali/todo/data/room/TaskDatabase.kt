package tech.androidplay.sonali.todo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 03/Oct/2020
 * Email: ankush@androidplay.in
 */

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao
}