package com.example.playlistmaker.library.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.db.PlaylistWithCountTracks
import com.example.playlistmaker.db.PlaylistsTrackEntity
import com.example.playlistmaker.db.SongToPlEntity
import com.example.playlistmaker.db.TrackDataBase
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.search.data.TrackDto
import java.util.Calendar
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val database: TrackDataBase,
    private val context: Context
) : PlaylistsRepository {

    private val filePath = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        PLAYLISTS_IMAGES
    )

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        var imageFileName: String? = null
        if (imageUri != null) {
            imageFileName = saveAlbumImage(imageUri)
        }
        database.playlistsDao().insertPlaylist(
            PlaylistEntity(
                null,
                playlistName,
                playlistDescription,
                imageFileName,
            )
        )
    }

    override suspend fun addTrack(track: TrackDto, playlistId: Int) =
        database.playlistsDao().addTrack(
            playlistsTrackEntity = mapToTrackEntity(track),
            trackPlaylistEntity = SongToPlEntity(null, playlistId, track.trackId)
        )

    override suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean =
        database
            .playlistsDao()
            .isTrackAlreadyExists(trackId, playlistId)


    override suspend fun getPlaylists(): List<Playlist> =
        convertPlaylistWithCountTracksToPlaylist(
            database.playlistsDao().getPlaylists()
        )

    override suspend fun getPlaylist(playlistId: Int): Playlist =
        mapToPlaylist(
            database.playlistsDao().getPlaylist(playlistId)
        )

    override suspend fun getPlaylistTracks(playlistId: Int): List<TrackDto> =
        convertPlaylistsTrackEntityToTrack(
            database
                .playlistsDao()
                .getPlaylistTracks(playlistId)
        )

    override suspend fun deleteTrack(trackId: Int, playlistId: Int) {
        database
            .playlistsDao()
            .deleteTrack(trackId, playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlist.cover?.let { deleteAlbumImage(it) }
        database
            .playlistsDao()
            .deletePlaylist(playlist.playlistId)
    }


    override suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        val playlist = database.playlistsDao().getPlaylist(playlistId)

        var imageFileName = playlist.cover
        if (imageUri != null) {
            if (playlist.cover != null) {
                deleteAlbumImage(playlist.cover)
            }
            imageFileName = saveAlbumImage(imageUri)
        }

        database.playlistsDao().updatePlaylist(
            PlaylistEntity(
                playlistId,
                playlistName,
                playlistDescription,
                imageFileName
            )
        )
    }

    private fun convertPlaylistsTrackEntityToTrack(tracks: List<PlaylistsTrackEntity>): List<TrackDto> =
        tracks.map { mapToTrackDto(it) }

    private fun convertPlaylistWithCountTracksToPlaylist(playListWithCountTracks: List<PlaylistWithCountTracks>): List<Playlist> =
        playListWithCountTracks.map {
            mapToPlaylist(it)
        }

    private fun saveAlbumImage(uri: Uri): String {
        val imageFileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
            .compress(
                Bitmap.CompressFormat.JPEG, 30,
                FileOutputStream(File(filePath, imageFileName))
            )
        return imageFileName
    }

    private fun deleteAlbumImage(imageFileName: String) {
        if (File(filePath, imageFileName).exists()) {
            File(filePath, imageFileName).delete()
        }
    }

    private fun mapToTrackEntity(track: TrackDto): PlaylistsTrackEntity {
        track.apply {
            return PlaylistsTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                artworkUrl60,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }

    private fun mapToTrackDto(playListsTrackEntity: PlaylistsTrackEntity): TrackDto {
        playListsTrackEntity.apply {
            return TrackDto(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                artworkUrl60,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }

    private fun mapToPlaylist(playlistWithCountTracks: PlaylistWithCountTracks): Playlist {
        playlistWithCountTracks.apply {
            return Playlist(
                playlistId!!,
                name,
                description,
                cover,
                tracksCount,
            )
        }
    }
}