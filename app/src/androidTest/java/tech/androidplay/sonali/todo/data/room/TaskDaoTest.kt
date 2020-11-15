package tech.androidplay.sonali.todo.data.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tech.androidplay.sonali.todo.getOrAwaitValue

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 14/Nov/2020
 * Email: ankush@androidplay.in
 */

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: TaskDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTaskItem() = runBlockingTest {
        val newTask = Task(
            "coHB3KUv5thOEGjiwrDiZyWlN9O2",
            "lb3Xm8eMri6cWdIbuXgv",
            "Late Alarm Experiment Text",
            "this is the alarm after 15-30mins gap. To see if the alarm notification still shows text.",
            "8.11.2020",
            "16:5",
            "Fri, 6 Nov 2020 13:09:39 +0530",
            false,
            "https://firebasestorage.googleapis.com/v0/b/think-ahead-275320.appspot.com/o/ankushbose5%40gmail.com%2Flb3Xm8eMri6cWdIbuXgv?alt=media&token=2f19fcbe-ea5a-4f8d-a8a2-747a6dcc1e97",
        )
        dao.insertTaskItem(newTask)

        val allTask = dao.observeAllTaskItem().getOrAwaitValue()

        assertThat(allTask).contains(newTask)
    }

    @Test
    fun deleteTaskItem() = runBlockingTest {
        val newTask = Task(
            "coHB3KUv5thOEGjiwrDiZyWlN9O4",
            "lb3Xm8eMri6cWdIbuXgv",
            "Late Alarm Experiment Text",
            "this is the alarm after 15-30mins gap. To see if the alarm notification still shows text.",
            "8.11.2020",
            "16:5",
            "Fri, 6 Nov 2020 13:09:39 +0530",
            false,
            "https://firebasestorage.googleapis.com/v0/b/think-ahead-275320.appspot.com/o/ankushbose5%40gmail.com%2Flb3Xm8eMri6cWdIbuXgv?alt=media&token=2f19fcbe-ea5a-4f8d-a8a2-747a6dcc1e97",
        )
        dao.insertTaskItem(newTask)
        dao.deleteTaskItem(newTask)

        val allTask = dao.observeAllTaskItem().getOrAwaitValue()

        assertThat(allTask).doesNotContain(newTask)
    }
}