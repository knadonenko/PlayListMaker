package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.search.data.TrackDto

@Database(version = 1, entities = [TrackEntity::class])
abstract class TrackDataBase : RoomDatabase() {
    abstract fun tracksDao(): TrackDto
}