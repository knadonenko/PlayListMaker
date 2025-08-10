package com.example.playlistmaker.library.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetMenuBinding
import com.example.playlistmaker.databinding.FragmentPlaylistMenuBottomSheetListDialogItemBinding
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.helpers.IntentConstants.PLAYLIST
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.viewModels.PlaylistMenuBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistMenuBottomSheet(private val playlist: Playlist, private val shareText: String) :
    BottomSheetDialogFragment() {

    private var _binding: BottomSheetMenuBinding? = null
    private val viewModel by viewModel<PlaylistMenuBottomSheetViewModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            BottomSheetMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            PLAYLISTS_IMAGES
        )
        Glide
            .with(binding.itemBottomSheet.namePlaylist)
            .load(playlist.cover?.let { File(filePath, it) })
            .placeholder(R.drawable.track_stub_big)
            .into(binding.itemBottomSheet.coverPlaylist)

        with(binding) {
            itemBottomSheet.namePlaylist.text = playlist.name
            itemBottomSheet.countTracks.text =
                itemBottomSheet.countTracks.resources.getQuantityString(
                    R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
                )

            itemBottomSheet.namePlaylist.text = playlist.name
            itemBottomSheet.countTracks.text =
                itemBottomSheet.countTracks.resources.getQuantityString(
                    R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
                )
        }

        binding.buttonSharing.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (playlist.tracksCount > 0) {
                    shareText(shareText, requireContext())
                } else {
                    dismiss()
                    Toast.makeText(
                        requireContext(), getString(R.string.empty_playlists),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.buttonEdit.setOnClickListener {
            dismiss()
            findNavController().navigate(
                R.id.single_to_player_action,
                Bundle().apply {
                    putParcelable(PLAYLIST, playlist)
                }
            )
        }

        binding.buttonDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.delete_playlist)
                    + " ${playlist.name}?")
                .setNegativeButton(getString(R.string.yes)) { _, _ -> }
                .setPositiveButton(getString(R.string.no)) { _, _ ->
                    viewModel.deletePlaylist(playlist) { findNavController().popBackStack() }
                }.show()
        }
    }

    companion object {
        const val TAG = "PlaylistBottomSheet"

        fun newInstance(playlist: Playlist, shareText: String): PlaylistMenuBottomSheet {
            return PlaylistMenuBottomSheet(playlist, shareText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun shareText(message: String, context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
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
}