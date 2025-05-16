package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

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

        binding.playButton.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        binding.addFavorites.apply {
            setOnClickListener {
                viewModel.addTrackToLiked(track)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            it.render(binding)
        }

        viewModel.setInitialTrack(track)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
    }

}