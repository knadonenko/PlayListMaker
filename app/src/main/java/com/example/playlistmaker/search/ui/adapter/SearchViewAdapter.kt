package com.example.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.network.SearchDiffCallBack

class SearchViewAdapter(private val clickListener: TrackClickListener,
                        private val longClickListener: LongTrackClickListener? = null) : RecyclerView.Adapter<SearchViewHolder>() {

    var tracks = ArrayList<TrackDto>()
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
        longClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                listener.onTrackLongClick(tracks[holder.adapterPosition])
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackDto)
    }

    fun interface LongTrackClickListener {
        fun onTrackLongClick(track: TrackDto)
    }

}