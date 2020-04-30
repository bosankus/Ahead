package tech.androidplay.sonali.todo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.adapter.PlantListAdapter
import tech.androidplay.sonali.todo.adapter.TodoListAdapter
import tech.androidplay.sonali.todo.data.model.PlanList
import tech.androidplay.sonali.todo.data.model.Todo
import tech.androidplay.sonali.todo.utils.TimeStampUtil

class HomeFragment : Fragment() {

    private val currentDate: String by lazy { TimeStampUtil().currentDate }
    private val currentTime: String by lazy { TimeStampUtil().currentTime }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPlanList()
        loadToDoList()
    }

    private fun loadPlanList() {
        rvPlanList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val planList = ArrayList<PlanList>()
        planList.add(
            PlanList(
                "Personal",
                "3"
            )
        )
        planList.add(
            PlanList(
                "Work",
                "2"
            )
        )
        planList.add(
            PlanList(
                "Work",
                "2"
            )
        )

        val adapter = PlantListAdapter(planList)
        rvPlanList.adapter = adapter
    }

    private fun loadToDoList() {
        rvTodoList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val todo = ArrayList<Todo>()
        todo.add(
            Todo(
                "1",
                "Get update from you broadband.",
                currentDate,
                currentTime
            )
        )
        todo.add(
            Todo(
                "2",
                "Get update from you Office",
                currentDate,
                currentTime
            )
        )
        val adapter = TodoListAdapter(todo)
        rvTodoList.adapter = adapter
    }


    companion object {
    }
}
