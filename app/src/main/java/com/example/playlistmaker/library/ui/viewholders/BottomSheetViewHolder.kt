package com.example.playlistmaker.library.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetItemBinding
import com.example.playlistmaker.library.data.Playlist

class BottomSheetViewHolder(private val binding: BottomSheetItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) = with(binding) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_2)

        namePlaylist.text = model.playlistName
        countTracks.text = itemView.resources.getQuantityString(
            R.plurals.tracks,
            model.tracksCount,
            model.tracksCount
        )

        Glide.with(itemView)
            .load(model.coverImageUrl)
            .placeholder(R.drawable.track_stub_big)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(coverPlaylist)
    }

}