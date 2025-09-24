package com.example.playlistmaker.player.ui

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.helpers.TimeHelper.convertDate
import com.example.playlistmaker.library.ui.fragments.PlayListBottomSheet
import com.example.playlistmaker.player.MusicService
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_NAME
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_TRACK
import com.example.playlistmaker.player.helpers.ServiceConstants.EXTRA_URL
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            viewModel.setPlayerControl(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = viewModel.getTrack()

        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.playButton.buttonClickListener = {
            viewModel.playbackControl()
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderPlayState(it)
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            renderLikeButton(it)
        }

        binding.addFavorites.apply {
            setOnClickListener {
                viewModel.addTrackToLiked()
            }
        }

        binding.addPlayList.setOnClickListener {
            PlayListBottomSheet.newInstance(track).show(childFragmentManager, PlayListBottomSheet.TAG)
        }

        renderTrackInfo()

        bindMusicService()

        requestNotificationPermission()

    }

    private fun renderTrackInfo() {
        binding.trackName.text = viewModel.getTrack().trackName
        binding.artistName.text = viewModel.getTrack().artistName
        binding.trackTime.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(viewModel.getTrack().trackTimeMillis)
        binding.albumName.text = viewModel.getTrack().collectionName
        binding.releaseDateData.text =
            convertDate(viewModel.getTrack().releaseDate!!)?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(
                    it
                )
            }
        binding.primaryGenreName.text = viewModel.getTrack().primaryGenreName
        binding.countryData.text = viewModel.getTrack().country

        Glide
            .with(binding.trackIcon)
            .load(viewModel.getTrack().artworkUrl100!!
                .replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.track_stub_big)
            .centerCrop()
            .transform(
                RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(R.dimen.margin_8))
            )
            .into(binding.trackIcon)
    }

    private fun renderPlayState(state: PlayerState) {
        binding.playButton.changeButtonState(state.buttonState)
        progressTimeViewUpdate(state.progress)
    }

    private fun progressTimeViewUpdate(progressTime: String) {
        binding.trackProgress.text = progressTime
    }

    private fun renderLikeButton(isFavorite: Boolean) {
        val imageResource = if (isFavorite) R.drawable.liked_icon
        else R.drawable.like_icon
        binding.addFavorites.setImageResource(imageResource)
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), REQUEST_NOTIFICATION_PERMISSION)
            }
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra(EXTRA_URL, viewModel.getTrack().previewUrl)
            putExtra(EXTRA_NAME, viewModel.getTrack().trackName)
            putExtra(EXTRA_TRACK, viewModel.getTrack().artistName)
        }
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    override fun onPause() {
        super.onPause()
        viewModel.showNotification()
    }

    override fun onResume() {
        super.onResume()
        viewModel.hideNotification()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindMusicService()
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1001
    }

}