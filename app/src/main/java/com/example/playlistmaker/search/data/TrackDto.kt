package com.example.playlistmaker.search.data

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var liked: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        return if (other !is TrackDto) {
            false
        } else {
            other.trackId == trackId
        }
    }
    override fun hashCode(): Int {
        return trackId
    }
}