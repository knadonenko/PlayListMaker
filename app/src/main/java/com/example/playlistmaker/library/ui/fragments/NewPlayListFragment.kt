package com.example.playlistmaker.library.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.example.playlistmaker.library.ui.states.CreateBtnState
import com.example.playlistmaker.library.ui.states.NewPlayListScreenState
import com.example.playlistmaker.library.ui.states.PermissionsResultState
import com.example.playlistmaker.library.ui.viewModels.NewPlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private val viewModel by viewModel<NewPlayListViewModel>()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

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

        initPickMediaRegister()

        initObserver()

        initListeners()

    }

    private fun initPickMediaRegister() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val cornerRadius =
                    requireContext().resources.getDimensionPixelSize(R.dimen.corner_8)

                Glide.with(this)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(cornerRadius))
                    .into(binding.playlistCoverImage)
                saveImageToPrivateStorage(uri)
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.screenStateFlow.collect { state ->
                when (state) {
                    is NewPlayListScreenState.AllowedToGoOut -> goBack()
                    is NewPlayListScreenState.Empty, is NewPlayListScreenState.HasContent -> renderCreateBtn(
                        state.createBtnState
                    )

                    is NewPlayListScreenState.NeedsToAsk -> showDialog()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.permissionStateFlow.collect { state ->
                when (state) {

                    PermissionsResultState.NEEDS_RATIONALE -> {

                        Toast
                            .makeText(
                                requireContext(),
                                getString(R.string.gallery_permission_message),
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }

                    PermissionsResultState.GRANTED -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    PermissionsResultState.DENIED_PERMANENTLY -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = Uri.fromParts("package", requireContext().packageName, null)
                        requireContext().startActivity(intent)
                    }
                }
            }
        }
    }

    private fun initListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            })

        binding.apply {

            toolbar.setNavigationOnClickListener {
                viewModel.onBackPressed()
            }

            playlistCoverImage.setOnClickListener {
                viewModel.onPlaylistCoverClicked()
            }

            playlistName.doOnTextChanged { text, _, _, _ ->

                renderBoxStrokeEditTextColor(binding.playlistNameContainer, text)
                viewModel.onPlaylistNameChanged(text.toString())

            }

            playlistDescription.doOnTextChanged { text, _, _, _ ->
                renderBoxStrokeEditTextColor(binding.playlistDescriptionContainer, text)
                viewModel.onPlaylistDescriptionChanged(text.toString())
            }

            buttonCreate.setOnClickListener {
                viewModel.onCreateBtnClicked()
                showSnackbar(playlistName.text.toString())
            }
        }
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    private fun renderCreateBtn(state: CreateBtnState) {
        when (state) {
            CreateBtnState.ENABLED -> binding.buttonCreate.isEnabled = true
            CreateBtnState.DISABLED -> binding.buttonCreate.isEnabled = false
        }
    }

    private fun renderBoxStrokeEditTextColor(view: TextInputLayout, text: CharSequence?) {
        if (!text.isNullOrEmpty()) {
            view.defaultHintTextColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.edittext_blue
            )
            ContextCompat
                .getColorStateList(requireContext(), R.color.edittext_blue)
                ?.let { view.setBoxStrokeColorStateList(it) }
        } else {
            view.defaultHintTextColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.edittext_color
            )
            ContextCompat
                .getColorStateList(requireContext(), R.color.edittext_color)
                ?.let { view.setBoxStrokeColorStateList(it) }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.title_playlist_dialog))
            .setMessage(getString(R.string.message_playlist_dialog))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.complete)) { _, _ -> goBack() }
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.my_playlists)
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment ?: "image")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        viewModel.saveImageUri(file.toURI())
    }

    private fun showSnackbar(playlistName: String) {
        val message =
            getString(R.string.playlist) + " \"" + playlistName + "\" " + getString(R.string.created)
        Snackbar
            .make(requireContext(), binding.fragmentNewPlaylist, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setDuration(2000)
            .show()
    }

}