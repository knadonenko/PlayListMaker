package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.helpers.AppConstants
import com.example.playlistmaker.helpers.IntentConstants
import com.example.playlistmaker.search.data.TrackDto
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra(IntentConstants.TRACK, TrackDto::class.java)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track = TrackDto(track!!.trackId, track.trackName,
                track.artistName, track.trackTimeMillis, track.artworkUrl100, track.collectionName,
                track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
            )
        )[PlayerViewModel::class.java]

        binding.playerToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.playButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        viewModel.state.observe(this) {
            it.render(binding)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

}