package tech.androidplay.sonali.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.androidplay.sonali.todo.data.model.Todo

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 4/19/2020, 11:20 PM
 */

@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = false
)
abstract class TodoRoomDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(context: Context): TodoRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}