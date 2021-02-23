package tech.androidplay.sonali.todo.view.adapter.viewpager_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import tech.androidplay.sonali.todo.databinding.LayoutAssignedCardListBinding
import tech.androidplay.sonali.todo.model.Todo
import tech.androidplay.sonali.todo.view.adapter.DiffUtilCallback

class ViewPagerAdapter : ListAdapter<Todo, ViewPagerViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutAssignedCardListBinding.inflate(layoutInflater, parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bind(todoItem)
    }


}