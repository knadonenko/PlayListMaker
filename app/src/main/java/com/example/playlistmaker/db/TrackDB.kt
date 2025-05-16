package com.example.playlistmaker.db

import android.util.Log
import com.example.playlistmaker.search.data.TrackDto

fun isTrackFavorite(trackDto: TrackDto) : Boolean {
    Log.d("IS FAVORITE!!!!!!!", trackDto.liked.toString())
    return trackDto.liked
}

var likedTracks: ArrayList<TrackDto> = ArrayList<TrackDto>()