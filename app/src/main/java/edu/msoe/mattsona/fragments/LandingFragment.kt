package edu.msoe.mattsona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {
    private var _binding: FragmentLandingBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)

        binding.btnViewStats.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.viewStats())
        }

        binding.btnNewRound.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.createNewRound())
        }

        binding.btnViewRounds.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.viewRounds())
        }

        return binding.root
    }
}