package com.example.playlistmaker.library.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.BottomSheetItemBinding
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.viewholders.BottomSheetViewHolder

class BottomSheetAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {

    val list = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetItemBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val playlistItem = list[holder.bindingAdapterPosition]
        holder.bind(playlistItem)
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlistItem) }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }

}