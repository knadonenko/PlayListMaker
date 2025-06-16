package com.example.playlistmaker.library.data

import com.example.playlistmaker.search.data.TrackDto

data class Playlist(val id: Int,
                    val coverImageUrl: String,
                    val playlistName: String,
                    val playlistDescription:String,
                    var trackList: List<TrackDto>,
                    var tracksCount: Int)
