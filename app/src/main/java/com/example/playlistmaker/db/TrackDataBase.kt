package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.search.data.TrackDto

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistsTrackEntity::class, SongToPlEntity::class])
abstract class TrackDataBase : RoomDatabase() {
    abstract fun tracksDao(): TracksDao
    abstract fun playlistsDao(): PlaylistDao
}