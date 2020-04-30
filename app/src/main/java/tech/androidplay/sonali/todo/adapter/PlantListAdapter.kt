package tech.androidplay.sonali.todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.androidplay.sonali.todo.R
import tech.androidplay.sonali.todo.data.model.PlanList

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 5/1/2020, 1:02 AM
 */
class PlantListAdapter(private val planList: ArrayList<PlanList>) :
    RecyclerView.Adapter<PlantListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home_plan_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = planList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binItems(planList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binItems(planList: PlanList) {
            val planCategory: TextView = itemView.findViewById(R.id.tvPlanCategory)
            val planDue: TextView = itemView.findViewById(R.id.tvPlantDue)

            planCategory.text = planList.planCategory
            planDue.text = planList.planDue
        }
    }
}