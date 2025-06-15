package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
//    @ColumnInfo(name = "playlist_name")
    val playlistName: String,
//    @ColumnInfo(name = "playlist_description")
    val playlistDescription: String,
//    @ColumnInfo(name = "cover_path")
    val imageUrl: String,
    val trackList: String,
    val countTracks: Int,
    val saveDate: Long
)