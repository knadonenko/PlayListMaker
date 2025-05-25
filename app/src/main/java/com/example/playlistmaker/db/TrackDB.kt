package com.example.playlistmaker.db

import com.example.playlistmaker.search.data.TrackDto

fun isTrackFavorite(trackDto: TrackDto): Boolean {
    return trackDto.liked
}

var likedTracks: ArrayList<TrackDto> = ArrayList<TrackDto>()