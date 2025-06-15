package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.db.TrackDataBase
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PlaylistRepositoryImpl(
    private val database: TrackDataBase
) : PlaylistsRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        database.playlistsDao().insertPlaylist(mapToPlayListEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.playlistsDao().deletePlaylist(mapToPlayListEntity(playlist))
    }

    override suspend fun updateTracks(playlist: Playlist) {
        database.playlistsDao().updatePlaylist(mapToPlayListEntity(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return database.playlistsDao().getSavedPlaylists()
            .map { playlist -> playlist.map { mapToPlayList(it) } }
    }

    private fun mapToPlayListEntity(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                imageUrl = coverImageUrl,
                trackList = Json.encodeToString(trackList),
                countTracks = tracksCount,
                Calendar.getInstance().timeInMillis,
            )
        }
    }

    private fun mapToPlayList(playlist: PlaylistEntity): Playlist {
        return with(playlist) {
            Playlist(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverImageUrl = imageUrl,
                trackList = Json.decodeFromString(trackList),
                tracksCount = countTracks,
            )
        }
    }
}