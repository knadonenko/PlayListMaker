package com.example.playlistmaker.api

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.model.Track

class SearchDiffCallBack(private val trackList: ArrayList<Track>,
                         private val newTrackList: ArrayList<Track>) : DiffUtil.Callback() {

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