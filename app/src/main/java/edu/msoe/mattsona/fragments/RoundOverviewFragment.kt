package edu.msoe.mattsona.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.databinding.FragmentRoundOverviewBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RoundOverviewFragment : Fragment() {
    private var _binding: FragmentRoundOverviewBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: RoundOverviewFragmentArgs by navArgs()
    private val viewmodel: GolfViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoundOverviewBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            val currentRound = viewmodel.getRound(args.roundId)
            val currentCourse = viewmodel.getCourse(currentRound.courseId)
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val roundString = "${currentCourse.name} | ${dateFormat.format(currentRound.date)}"

            binding.roundSubtitle.text = roundString

            binding.holesText.text = "${currentRound.holes}"

            val score = viewmodel.calculateTotalScore(args.roundId)
            if (score == -1) {
                binding.roundScore.text = "N/A"
            } else {
                binding.roundScore.text = "$score"
            }
        }

        binding.btnEditScoring.setOnClickListener {
            findNavController().navigate(RoundOverviewFragmentDirections.editSelectedRound(args.roundId))
        }

        return binding.root
    }
}