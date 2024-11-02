package com.example.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.viewholders.Search

class SearchViewAdapter(private val items: ArrayList<Track>) : RecyclerView.Adapter<Search>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Search {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return Search(view)
    }

    override fun onBindViewHolder(holder: Search, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}