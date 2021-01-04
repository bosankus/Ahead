package tech.androidplay.sonali.todo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_intro_screen.view.*
import tech.androidplay.sonali.todo.R

/**
 * Created by Androidplay
 * Author: Ankush
 * On: 28/Dec/2020
 * Email: ankush@androidplay.in
 */
class ViewPagerAdapter(private var introTextList: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val introText: TextView = itemView.introScreenTextItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_intro_screen, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.introText.text = introTextList[position]
    }

    override fun getItemCount() = introTextList.size


}