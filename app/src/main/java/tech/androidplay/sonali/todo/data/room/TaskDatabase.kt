package tech.androidplay.sonali.todo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Nov/2020
 * Email: ankush@androidplay.in
 */

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}