package com.example.playlistmaker.library.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val cover: String?,
    var tracksCount: Int
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return if (other !is Playlist) {
            false
        } else {
            other.playlistId == playlistId
        }
    }

    override fun hashCode(): Int {
        return playlistId
    }
}
