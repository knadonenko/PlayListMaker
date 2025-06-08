package com.example.playlistmaker.library.data.impl

import com.example.playlistmaker.db.TrackDataBase
import com.example.playlistmaker.db.TrackEntity
import com.example.playlistmaker.library.domain.LibraryRepository
import com.example.playlistmaker.search.data.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class FavoritesRepositoryImpl(private val database: TrackDataBase) : LibraryRepository {
    override suspend fun saveTrack(track: TrackDto) {
        database.tracksDao().saveTrack(mapToTrackEntity(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        database.tracksDao().deleteTrack(trackId)
    }

    override fun getFavoritesTracks(): Flow<ArrayList<TrackDto>> = flow {
        val tracks = database
            .tracksDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks as ArrayList<TrackEntity>))
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> = flow {
        val isInFavorite = database.tracksDao().isFavorite(trackId)
        emit(isInFavorite)
    }

    private fun convertFromTrackEntity(tracks: ArrayList<TrackEntity>): ArrayList<TrackDto> {
        return tracks.map { track -> mapToTrackDTO(track) } as ArrayList<TrackDto>
    }

    private fun mapToTrackEntity(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
            Calendar.getInstance().timeInMillis
        )
    }

    private fun mapToTrackDTO(track: TrackEntity): TrackDto {
        return TrackDto(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl
        )
    }
}