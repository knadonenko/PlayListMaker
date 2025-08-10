package com.example.playlistmaker.library.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.viewholders.PlaylistViewHolder

abstract class PlaylistAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = mutableListOf<Playlist>()

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlistItem = playlists[holder.adapterPosition]
        holder.apply {
            bind(playlistItem)
            itemView.setOnClickListener { clickListener.onTrackClick(playlistItem) }
        }
    }

    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: Playlist)
    }
}