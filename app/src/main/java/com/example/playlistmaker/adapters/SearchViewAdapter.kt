package com.example.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.api.SearchDiffCallBack
import com.example.playlistmaker.data.dto.Track
import com.example.playlistmaker.viewholders.SearchViewHolder

class SearchViewAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<SearchViewHolder>() {

    var tracks = ArrayList<Track>()
        set(newTrackList) {
            val diffResult = DiffUtil.calculateDiff(
                SearchDiffCallBack(field, newTrackList)
            )
            field = newTrackList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.apply {
            bind(tracks[position])
            itemView.setOnClickListener {
                clickListener.onTrackClick(tracks[position])
            }
        }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

}