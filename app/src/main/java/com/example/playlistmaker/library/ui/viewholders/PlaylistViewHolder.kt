package com.example.playlistmaker.library.ui.viewholders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.library.data.Playlist
import java.io.File

class PlaylistViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val playlistCover: ImageView = itemView.findViewById(R.id.playlist_cover)
    private val playlistName: TextView = itemView.findViewById(R.id.playlist_name)
    private val tracksCount: TextView = itemView.findViewById(R.id.playlist_tracks)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        tracksCount.text = tracksCount.resources.getQuantityString(
            R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
        )

        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_8)


        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES
        )
        Glide
            .with(itemView)
            .load(playlist.cover?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.track_stub_big)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(playlistCover)

    }

}