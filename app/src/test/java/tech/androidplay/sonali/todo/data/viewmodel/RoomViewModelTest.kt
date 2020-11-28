package tech.androidplay.sonali.todo.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.androidplay.sonali.todo.data.getOrAwaitValueTest
import tech.androidplay.sonali.todo.data.room.FakeRoomRepository
import tech.androidplay.sonali.todo.data.room.Task
import tech.androidplay.sonali.todo.utils.Event
import tech.androidplay.sonali.todo.utils.Resource
import tech.androidplay.sonali.todo.utils.Status

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 26/Nov/2020
 * Email: ankush@androidplay.in
 */
class RoomViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RoomViewModel

    @Before
    fun setup() {
        viewModel = RoomViewModel(FakeRoomRepository())
    }

    @Test
    fun insertEmptyFields_returnFalse() {
        viewModel.insertTask(
            "coHB3KUv5thOEGjiwrDiZyWlN9O2",
            "lb3Xm8eMri6cWdIbuXgv",
            "",
            "this is the alarm after 15-30mins gap. To see if the alarm notification still shows text.",
            "8.11.2020",
            "16:5",
            "Fri, 6 Nov 2020 13:09:39 +0530",
            false,
            "https://firebasestorage.googleapis.com/v0/b/think-ahead-275320.appspot.com/o/ankushbose5%40gmail.com%2Flb3Xm8eMri6cWdIbuXgv?alt=media&token=2f19fcbe-ea5a-4f8d-a8a2-747a6dcc1e97",
        )
        val value: Event<Resource<Task>> = viewModel.insertTaskItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }
}