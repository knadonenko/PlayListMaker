package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.helpers.IntentConstants
import com.example.playlistmaker.search.data.TrackDto
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra(IntentConstants.TRACK, TrackDto::class.java)

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

        viewModel.setInitialTrack(track!!)
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