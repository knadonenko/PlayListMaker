package com.example.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetBinding
import com.example.playlistmaker.helpers.IntentConstants.TRACK
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.adapters.BottomSheetAdapter
import com.example.playlistmaker.library.ui.states.BottomSheetState
import com.example.playlistmaker.library.ui.viewModels.BottomSheetViewModel
import com.example.playlistmaker.search.data.TrackDto
import com.google.android.material.R.id.design_bottom_sheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null
    private val viewModel by viewModel<BottomSheetViewModel>()

    private lateinit var playlistsAdapter: BottomSheetAdapter
    private lateinit var track: TrackDto

    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        setupRatio(dialog as BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        track = requireArguments()
            .getString(TRACK)
            ?.let { Json.decodeFromString<TrackDto>(it) } !!

        playlistsAdapter = BottomSheetAdapter { playlist ->
            viewModel.onPlaylistClicked(playlist, track)
        }
        binding.playlistsRecycler.adapter = playlistsAdapter

        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.bottomSheet_to_newPlaylist
            )
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }
    }

    private fun render(state: BottomSheetState) {
        when (state) {
            is BottomSheetState.AddedAlready -> {
                val message =
                    getString(R.string.already_added) + " \"" + state.playlistModel.playlistName + "\" "
                Toast
                    .makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
            }

            is BottomSheetState.AddedNow -> {
                val message =
                    getString(R.string.added) + " \"" + state.playlistModel.playlistName + "\" "

                showMessage(message)
                dialog?.cancel()
            }

            else -> showContent(state.content)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
                requireContext(),
                requireActivity().findViewById(R.id.player),
                message,
                Snackbar.LENGTH_SHORT
            )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setDuration(2000)
            .show()
    }

    private fun showContent(content: List<Playlist>) {
        binding.playlistsRecycler.visibility = View.VISIBLE
        playlistsAdapter.apply {
            list.clear()
            list.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 100 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()

        requireActivity().windowManager.defaultDisplay.getMetrics(
            displayMetrics
        )

        return displayMetrics.heightPixels
    }

    companion object {

        fun createArgs(track: TrackDto): Bundle = bundleOf(
            TRACK to Json.encodeToString(track)
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}