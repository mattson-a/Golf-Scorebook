package edu.msoe.mattsona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.HoleStatisticListAdapter
import edu.msoe.mattsona.R
import edu.msoe.mattsona.databinding.FragmentRoundScoringBinding
import edu.msoe.mattsona.entities.HoleStatistic
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RoundScoringFragment : Fragment() {
    private var _binding: FragmentRoundScoringBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewmodel: GolfViewModel by viewModels()
    private val args: RoundScoringFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoundScoringBinding.inflate(inflater, container, false)
        binding.holeRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val currentRound = viewmodel.getRound(args.roundId)
            val holeStatistics = mutableListOf<HoleStatistic>()
            val possiblePars = resources.getIntArray(R.array.possibleHolePars).asList()

            for (i in 1..currentRound.holes) {
                //create "holes" many holeStatistic objects for our recycler for user input
                val hole = HoleStatistic(roundId = currentRound.id, holeNumber = i, holePar = null, holeScore = null)
                holeStatistics.add(hole)
            }

            binding.holeRecycler.adapter = HoleStatisticListAdapter(holeStatistics, possiblePars)

            val currentCourse = viewmodel.getCourse(currentRound.courseId)

            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val roundSubtitleText = "${currentCourse.name} | ${dateFormat.format(currentRound.date)}"
            binding.roundDetails.text = roundSubtitleText

            binding.btnSaveRound.setOnClickListener {
                viewmodel.insertHoleStatistics(holeStatistics)
                findNavController().navigate(RoundScoringFragmentDirections.returnToLanding())
            }
        }
    }
}