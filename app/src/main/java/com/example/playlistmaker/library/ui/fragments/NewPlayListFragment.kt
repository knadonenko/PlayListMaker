package com.example.playlistmaker.library.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.helpers.AppConstants.PLAYLISTS_IMAGES
import com.example.playlistmaker.helpers.IntentConstants.PLAYLIST
import com.example.playlistmaker.library.data.Playlist
import com.example.playlistmaker.library.ui.viewModels.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private val viewModel by viewModel<NewPlayListViewModel>()
    private var playlist: Playlist? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist?.let {
            binding.toolbar.title = getString(R.string.edit_info)
            binding.buttonCreate.text = getString(R.string.save_info)
            binding.playlistName.setText(it.name)
            binding.playlistDescription.setText(it.description)
            it.cover?.let { imageName ->
                binding.playlistCoverImage.setImageURI(
                    File(
                        File(
                            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            PLAYLISTS_IMAGES
                        ), imageName
                    ).toUri()
                )
            }
            binding.buttonCreate.isEnabled = true
        }

        binding.playlistName.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.buttonCreate.isEnabled = !s.isNullOrEmpty()
        }

        binding.buttonCreate.setOnClickListener {
            if (!viewModel.isClickable)
                return@setOnClickListener
            viewModel.onBtnClick()

            val name = binding.playlistName.text.toString()
            val description = binding.playlistDescription.text.toString()
            if (playlist != null) {
                viewModel.updatePlaylist(
                    playlist!!.playlistId,
                    name = name,
                    description = description,
                    imageUri = imageUri
                ) {
                    findNavController().popBackStack()
                }
            } else {
                viewModel.createPlaylist(
                    name = name,
                    description = description,
                    imageUri = imageUri
                ) {
                    Toast.makeText(
                        requireContext(),
                        String.format(
                            resources.getText(R.string.created).toString(),
                            name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                }

            }
        }

        binding.toolbar.setOnClickListener {
            if (checkInput()) {
                showDialog()
            } else {
                findNavController().popBackStack()
            }
        }

        initPickMediaRegister()

        initBackPressed()

    }

    private fun initPickMediaRegister() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistCoverImage.setImageURI(uri)
                    imageUri = uri
                }
            }
        binding.playlistCoverImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playlist = arguments?.getParcelable(PLAYLIST, Playlist::class.java)
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.title_playlist_dialog))
            .setMessage(getString(R.string.message_playlist_dialog))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.complete)) { _, _ -> goBack() }
            .show()
    }

    private fun checkInput(): Boolean {
        playlist?.let {
            return false
        }
        return (
                imageUri != null
                        || binding.playlistName.text.toString().isNotEmpty()
                        || binding.playlistDescription.text.toString().isNotEmpty()
                )
    }

    private fun initBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkInput()) {
                    showDialog()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}