package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_to_pl")
class SongToPlEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playlistId: Int,
    val trackId: Int
)