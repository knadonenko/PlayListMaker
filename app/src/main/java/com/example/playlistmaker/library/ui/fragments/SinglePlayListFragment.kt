package com.example.playlistmaker.library.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlayListBinding
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.helpers.IntentConstants
import com.example.playlistmaker.helpers.IntentConstants.TRACK
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.states.PlaylistState
import com.example.playlistmaker.library.ui.viewModels.PlaylistViewModel
import com.example.playlistmaker.search.data.TrackDto
import com.example.playlistmaker.search.ui.adapter.SearchViewAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class SinglePlayListFragment : Fragment() {

    private lateinit var binding: FragmentSinglePlayListBinding
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var playlist: Playlist

    private val trackAdapter = SearchViewAdapter({ clickOnTrack(it) }, { longClickOnTrack(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSinglePlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.PlaylistTracks -> {
                    showTracks(it.tracks)
                }

                is PlaylistState.PlaylistInfo -> {
                    playlist = it.playlist
                    showPlaylist()
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.tracksRecycler.adapter = trackAdapter

        binding.share.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (trackAdapter.tracks.isNotEmpty()) {
                    shareText(createMessageForShare(), requireContext())
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.share_empty_play_list),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.menu.setOnClickListener {
            PlaylistMenuBottomSheet.newInstance(playlist, createMessageForShare())
                .show(childFragmentManager, PlaylistMenuBottomSheet.TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlayListInfo(playlist.playlistId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlist = arguments?.getParcelable(IntentConstants.PLAYLIST, Playlist::class.java)!!
    }

    private fun showTracks(tracks: List<TrackDto>) {
        with(binding) {
            if (tracks.isEmpty()) {
                nothingFound.visibility = View.VISIBLE
                nothingFoundText.visibility = View.VISIBLE
            }
            trackAdapter.tracks = tracks as ArrayList<TrackDto>
            trackAdapter.notifyDataSetChanged()
            var totalDuration = 0L
            trackAdapter.tracks.forEach { track ->
                totalDuration += track.trackTimeMillis.toLong()
            }
            totalDuration = TimeUnit.MILLISECONDS.toMinutes(totalDuration)

            length.text = length.resources.getQuantityString(
                R.plurals.minutes,
                totalDuration.toInt(),
                totalDuration
            )

            trackCount.text = trackCount.resources.getQuantityString(
                R.plurals.tracks,
                trackAdapter.tracks.size,
                trackAdapter.tracks.size
            )
        }
    }

    private fun showPlaylist() {
        with(binding) {
            val filePath = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PLAYLISTS_IMAGES
            )
            Glide
                .with(playlistCover)
                .load(playlist.cover?.let { imageName -> File(filePath, imageName) })
                .placeholder(R.drawable.track_stub_big)
                .into(playlistCover)

            playlistName.text = playlist.name
            playlistName.isSelected = true

            if (playlist.description.isNotEmpty()) {
                description.text = playlist.description
                description.isSelected = true
                description.visibility = View.VISIBLE
            } else {
                description.visibility = View.GONE
            }
        }

        initBottomSheet()
    }

    private fun initBottomSheet() {
        with(binding) {
            val bottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheet)
            shareLine.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        bottomSheetBehavior.peekHeight = shareLine.height
                        shareLine.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )
        }
    }

    private fun clickOnTrack(track: TrackDto) {
        if (viewModel.clickDebounce()) {
            findNavController().navigate(
                R.id.single_to_player_action,
                Bundle().apply {
                    putParcelable(TRACK, track)
                }
            )
        }
    }

    private fun longClickOnTrack(track: TrackDto) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.delete_track))
            .setNegativeButton(getString(R.string.yes)) { _, _ -> }
            .setPositiveButton(getString(R.string.no)) { _, _ ->
                viewModel.deleteTrack(track.trackId, playlist.playlistId)
            }.show()
    }

    private fun shareText(message: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,message)
        intent.type = "text/plain"
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                context.getString(R.string.apps_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createMessageForShare(): String {
        var message =
            "${playlist.name}\n${playlist.description}\n" + binding.trackCount.resources.getQuantityString(
                R.plurals.tracks,
                trackAdapter.tracks.size,
                trackAdapter.tracks.size
            ) + "\n"
        trackAdapter.tracks.forEachIndexed { index, track ->
            message += "\n ${index + 1}. ${track.artistName} - ${track.trackName} (" + SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis) + ")"
        }
        return message
    }
}