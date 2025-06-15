package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getSavedPlaylists()
    }

    override fun isTrackAddedToPlaylist(playlist: Playlist, track: TrackDto): Boolean {
        return playlist.trackList.contains(track)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: TrackDto) {
        playlist.trackList += track
        playlist.tracksCount = playlist.trackList.size
        repository.updateTracks(playlist)
    }
}