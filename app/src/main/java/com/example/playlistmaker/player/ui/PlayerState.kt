package com.example.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.helpers.TimeHelper.convertDate
import com.example.playlistmaker.search.data.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState {

    class BeginningState(val track: TrackDto) : PlayerState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis.toLong())
            binding.albumName.text = track.collectionName
            binding.releaseDateData.text =
                convertDate(track.releaseDate!!)?.let {
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(
                        it
                    )
                }
            binding.primaryGenreName.text = track.primaryGenreName
            binding.countryData.text = track.country

            Glide
                .with(binding.trackIcon)
                .load(track.artworkUrl100!!.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.track_stub_big)
                .centerCrop()
                .transform(
                    RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(R.dimen.margin_8))
                )
                .into(binding.trackIcon)
        }
    }

    class PlayButtonHandling(private val playerStateEnum: PlayerViewModel.PlayerStateEnum) : PlayerState() {
        override fun render(binding: FragmentPlayerBinding) {
            when (playerStateEnum) {
                PlayerViewModel.PlayerStateEnum.STATE_PLAYING -> {
                    binding.playButton.setBackgroundResource(R.drawable.pause_circle)
                }

                else -> {
                    binding.playButton.setBackgroundResource(R.drawable.play_circle)
                }
            }
        }
    }

    class Preparing : PlayerState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playButton.isEnabled = true
        }
    }

    class TimerUpdating(private val time: String) : PlayerState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.trackProgress.text = time
        }
    }

    class PlayCompleting : PlayerState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.trackProgress.text =
                binding.trackProgress.resources.getText(R.string.progress_time)
            binding.playButton.setImageResource(R.drawable.play_circle)
        }
    }

    abstract fun render(binding: FragmentPlayerBinding)

}