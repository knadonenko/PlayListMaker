package com.example.playlistmaker.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_NAME
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_TRACK
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_URL
import com.example.playlistmaker.player.ui.AudioPlayerControl
import com.example.playlistmaker.player.ui.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

internal class MusicService : Service(), AudioPlayerControl {

    private var trackName: String? = null
    private var artistName: String? = null
    private var trackUrl: String? = null
    private val binder = MusicServiceBinder()
    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    private val playerState = _playerState.asStateFlow()
    private var notificationIsShown = false

    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(200L)
                _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        trackName = intent?.getStringExtra(EXTRA_TRACK)
        artistName = intent?.getStringExtra(EXTRA_NAME)
        trackUrl = intent?.getStringExtra(EXTRA_URL)
        initMediaPlayer()
        createNotificationChannel()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    private fun initMediaPlayer() {
        if (trackUrl.isNullOrBlank()) return
        mediaPlayer?.setDataSource(trackUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.value = PlayerState.Finished()
            hideNotification()
        }
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Playing music"
        channel.setSound(null, null)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(this.resources.getString(R.string.app_name))
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = PlayerState.Playing(getCurrentPlayerPosition())
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun showNotification(){
        if (_playerState.value is PlayerState.Playing) {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                createServiceNotification(),
                getForegroundServiceTypeConstant()
            )
            notificationIsShown = true
        }
    }

    override fun hideNotification(){
        if (notificationIsShown){
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
            notificationIsShown = !notificationIsShown
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss",
            Locale.getDefault()).format(mediaPlayer?.currentPosition) ?: "00:00"
    }

    companion object {
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val CHANNEL_NAME = "Music Player"
    }

}