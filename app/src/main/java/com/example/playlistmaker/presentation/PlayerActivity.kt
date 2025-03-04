package com.example.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.helpers.AppConstants.RELOAD_PROGRESS
import com.example.playlistmaker.helpers.AppConstants.TIME_ZERO
import com.example.playlistmaker.helpers.IntentConstants
import com.example.playlistmaker.helpers.TimeHelper.convertDate
import com.example.playlistmaker.helpers.TimeHelper.convertTimeFromMillis
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumIcon: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var trackProgress: TextView
    private lateinit var playButton: ImageButton

    private val play = R.drawable.play_circle
    private val pause = R.drawable.pause_circle

    private var mainHandler: Handler? = null

    private var mediaPlayer = MediaPlayer()

    private val runThread = object: Runnable {
        override fun run() {
            trackProgress.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)

            mainHandler?.postDelayed(
                this,
                RELOAD_PROGRESS
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<Toolbar>(R.id.player_toolbar).setNavigationOnClickListener {
            finish()
        }

        val track = Gson().fromJson(intent.getStringExtra(IntentConstants.TRACK),
            TrackDto::class.java)

        mainHandler = Handler(Looper.getMainLooper())

        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.track_time)
        albumIcon = findViewById(R.id.track_icon)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name)
        country = findViewById(R.id.country_data)
        trackProgress = findViewById(R.id.track_progress)
        playButton = findViewById(R.id.play_button)

        Glide
            .with(albumIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_stub_big)
            .centerCrop()
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(
                        R.dimen.margin_8
                    )
                )
            )
            .into(albumIcon)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        trackTime.text = convertTimeFromMillis(track.trackTimeMillis)

        val date = convertDate(track.releaseDate)
        if (date != null) {
            val formatDatesString = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            releaseDate.text = formatDatesString
        }

        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            collectionNameTitle.visibility = View.GONE
        }

        preparePlayer(track)

        playButton.setOnClickListener {
            playbackControl()
            progressTimeControl()
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainHandler?.removeCallbacks(runThread)
            playButton.setBackgroundResource(play)
            trackProgress.text = TIME_ZERO
        }
    }

    private fun preparePlayer(track: TrackDto) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setBackgroundResource(play)
            playerState = STATE_PREPARED
        }
    }

    private fun progressTimeControl() {
        when (playerState) {
            STATE_PLAYING -> {
                mainHandler?.postDelayed(
                    runThread,
                    RELOAD_PROGRESS
                )
            }
            STATE_PAUSED -> {
                mainHandler?.removeCallbacks(runThread)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setBackgroundResource(pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setBackgroundResource(play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainHandler?.removeCallbacks(runThread)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT

}