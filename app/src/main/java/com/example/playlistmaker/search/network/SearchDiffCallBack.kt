package com.example.playlistmaker.search.network

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.search.data.TrackDto

class SearchDiffCallBack(private val trackList: ArrayList<TrackDto>,
                         private val newTrackList: ArrayList<TrackDto>) : DiffUtil.Callback() {

    override fun getOldListSize() = trackList.size

    override fun getNewListSize() = newTrackList.size

    override fun areItemsTheSame(itemPosition: Int, newItemPosition: Int): Boolean {
        val track = trackList[itemPosition]
        val newTrack = newTrackList[newItemPosition]
        return track.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(itemPosition: Int, newItemPosition: Int): Boolean {
        val track = trackList[itemPosition]
        val newTrack = newTrackList[newItemPosition]
        return track == newTrack
    }

}