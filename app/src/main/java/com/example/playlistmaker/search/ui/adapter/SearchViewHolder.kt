package com.example.playlistmaker.search.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchItemBinding
import com.example.playlistmaker.helpers.TimeHelper.longToMillis
import com.example.playlistmaker.search.data.TrackDto

class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding = SearchItemBinding.bind(itemView)

    fun bind(track: TrackDto) = with(binding) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTimeMillis.longToMillis()

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_stub)
            .transform(RoundedCorners(2))
            .into(trackIcon)
    }

}