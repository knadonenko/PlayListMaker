package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.LibraryInteractor
import com.example.playlistmaker.library.domain.LibraryRepository
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: LibraryRepository) : LibraryInteractor {
    override suspend fun saveTrack(track: TrackDto) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override fun getFavoriteTracks(): Flow<ArrayList<TrackDto>> {
        return repository.getFavoritesTracks()
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> {
        return repository.isFavorite(trackId)
    }
}