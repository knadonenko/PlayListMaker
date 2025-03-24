package com.example.playlistmaker.search.navigation

import android.app.Activity
import android.content.Intent
import com.example.playlistmaker.helpers.IntentConstants.TRACK
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.data.TrackDto

class Router(private val activity: Activity) {

    fun goBack() {
        activity.finish()
    }

    fun openAudioPlayer(track: TrackDto) {
        val intent = Intent(activity, PlayerActivity::class.java).apply {
            putExtra(TRACK, track)
        }
        activity.startActivity(intent)
    }

}