package com.example.playlistmaker.search.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
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
    val previewUrl: String
) : Parcelable {

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