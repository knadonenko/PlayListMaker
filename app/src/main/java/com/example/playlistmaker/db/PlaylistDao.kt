package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(entity = PlaylistsTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylistsTrack(playlistsTrackEntity: PlaylistsTrackEntity)

    @Insert(entity = SongToPlEntity::class)
    suspend fun addTrackPlaylist(trackPlaylistEntity: SongToPlEntity)

    @Transaction
    suspend fun addTrack(
        playlistsTrackEntity: PlaylistsTrackEntity,
        trackPlaylistEntity: SongToPlEntity
    ) {
        addPlaylistsTrack(playlistsTrackEntity)
        addTrackPlaylist(trackPlaylistEntity)
    }

    @Query("SELECT EXISTS (SELECT 1 FROM song_to_pl" +
            " WHERE trackId = :trackId AND playlistId = :playlistId)")
    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean

    @Query("DELETE FROM song_to_pl WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromTrackPlayList(playlistId: Int, trackId: Int)

    @Transaction
    suspend fun deleteTrack(
        trackId: Int,
        playlistId: Int
    ) {
        deleteTrackFromTrackPlayList(playlistId, trackId)
        clearTracks()
    }

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromPlaylist(playlistId: Int)

    @Query("DELETE FROM song_to_pl WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromTrackPlaylist(playlistId: Int)

    @Query("DELETE FROM track_playlists " +
            "WHERE trackId NOT IN (SELECT DISTINCT(trackId) FROM song_to_pl)")
    suspend fun clearTracks()

    @Transaction
    suspend fun deletePlaylist(playlistId: Int) {
        deletePlaylistFromPlaylist(playlistId)
        deletePlaylistFromTrackPlaylist(playlistId)
        clearTracks()
    }

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT playlistId, name, description, cover, " +
            "(SELECT COUNT(id) FROM song_to_pl as spl " +
            "WHERE spl.playlistId=playlists.playlistId) as tracksCount " +
            "FROM playlists " +
            "ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistWithCountTracks>

    @Query("SELECT playlistId, name, description, cover, " +
            "(SELECT COUNT(id) FROM song_to_pl as spl " +
            "WHERE spl.playListId=playlists.playlistId) as tracksCount " +
            "FROM playlists WHERE playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistWithCountTracks

    @Query("SELECT track_playlists.* FROM track_playlists " +
            "LEFT JOIN song_to_pl as spl ON track_playlists.trackId=spl.trackId " +
            "WHERE spl.playListId = :playlistId  " +
            "ORDER BY spl.id DESC")
    suspend fun getPlaylistTracks(playlistId: Int): List<PlaylistsTrackEntity>


}