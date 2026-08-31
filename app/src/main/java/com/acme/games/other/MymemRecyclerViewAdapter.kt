package com.acme.games.other

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.acme.games.R

import com.acme.games.other.placeholder.PlaceholderContent.PlaceholderItem
import com.acme.games.databinding.FragmentItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MymemRecyclerViewAdapter(
    private var values: List<Int>,
    private var vis: List<Boolean>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MymemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.toString()

        if (item == -1 || !vis[position]) {
            holder.contentView.visibility = View.INVISIBLE
        } else {
            holder.contentView.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun updateValues(newValues: List<Int>) {
        this.values = newValues
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content


    }

}