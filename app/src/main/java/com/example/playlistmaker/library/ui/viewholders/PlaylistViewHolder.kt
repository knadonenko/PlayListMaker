package com.example.playlistmaker.library.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.library.data.Playlist

class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) = with(binding) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_8)

        playlistName.text = model.playlistName
        playlistTracks.text = itemView.resources.getQuantityString(
            R.plurals.tracks,
            model.tracksCount,
            model.tracksCount
        )

        Glide.with(itemView)
            .load(model.coverImageUrl)
            .placeholder(R.drawable.track_stub_big)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(playlistCover)
    }

}