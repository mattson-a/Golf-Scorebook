package edu.msoe.mattsona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.databinding.FragmentViewStatsBinding
import edu.msoe.mattsona.entities.Course
import edu.msoe.mattsona.entities.Round
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ViewStatsFragment : Fragment() {
    private var _binding: FragmentViewStatsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewmodel: GolfViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewStatsBinding.inflate(inflater, container, false)

        val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())

        lifecycleScope.launch {
            val rounds = viewmodel.getAllRounds()

            //calculate low/high rounds
            var high9 = 0
            var high9round: Round? = null

            var high18 = 0
            var high18round: Round? = null

            var low9 = Int.MAX_VALUE
            var low9round: Round? = null

            var low18 = Int.MAX_VALUE
            var low18round: Round? = null

            for (r in rounds) {
                val score = calculateTotalScore(r.id)
                if (r.holes == 18) {
                    //18 hole tests
                    if (score != -1) {
                        if (score > high18) {
                            high18 = score
                            high18round = r
                        }
                        if (score < low18) {
                            low18 = score
                            low18round = r
                        }
                    }
                } else {
                    //9 hole tests
                    if (score != -1) {
                        if (score > high9) {
                            high9 = score
                            high9round = r
                        }
                        if (score < low9) {
                            low9 = score
                            low9round = r
                        }
                    }
                }
            }

            //set text for rounds appropriately
            if (high9 == 0) {
                //initial condition
                binding.high9.text = "N/A"
            } else {
                val scoreToPar = calculateScoreToPar(high9round!!.id, high9)
                val string = "$high9 ($scoreToPar) | ${dateFormat.format(high9round.date)}"
                binding.high9.text = string
            }

            if (low9 == Int.MAX_VALUE) {
                //initial condition
                binding.low9.text = "N/A"
            } else {
                val scoreToPar = calculateScoreToPar(low9round!!.id, low9)
                val string = "$low9 ($scoreToPar) | ${dateFormat.format(low9round.date)}"
                binding.low9.text = string
            }

            if (high18 == 0) {
                //initial condition
                binding.high18.text = "N/A"
            } else {
                val scoreToPar = calculateScoreToPar(high18round!!.id, high18)
                val string = "$high18 ($scoreToPar) | ${dateFormat.format(high18round.date)}"
                binding.high18.text = string
            }

            if (low18 == Int.MAX_VALUE) {
                //initial condition
                binding.low18.text = "N/A"
            } else {
                val scoreToPar = calculateScoreToPar(low18round!!.id, low18)
                val string = "$low18 ($scoreToPar) | ${dateFormat.format(low18round.date)}"
                binding.low18.text = string
            }

            //populate recent scores
            val recentRounds = rounds.sortedByDescending { it.date }.take(3)
            var recentString = ""
            if (recentRounds.isEmpty()) {
                recentString = "N/A"
            } else {
                for (r in recentRounds) {
                    val score = calculateTotalScore(r.id)
                    recentString += if (score != -1) {
                        "$score | ${dateFormat.format(r.date)}\n"
                    } else {
                        "N/A | ${dateFormat.format(r.date)}\n"
                    }
                }
            }

            binding.recentScores.text = recentString.trim()

            //populate top courses
            val courses = viewmodel.getAllCourses()

            val roundCountByCourse: Map<Long, Int> = rounds.groupingBy { it.courseId }.eachCount()
            val topCourses: List<Pair<Course, Int>> = courses.map { c ->
                val count = roundCountByCourse[c.id] ?: 0
                c to count
            }.sortedByDescending { it.second }.take(3)

            var topCoursesString = ""
            if (topCourses.isEmpty()) {
                topCoursesString = "N/A"
            } else {
                for ((course, count) in topCourses) {
                    topCoursesString += course.name + " ($count)\n"
                }
            }

            binding.topCourses.text = topCoursesString.trim()

            val parPlusPercent = calculateParOrBetterPercent()
            var parPercentText = ""

            if (parPlusPercent != -1.0) {
                parPercentText = "${String.format(Locale.getDefault(),"%.2f", parPlusPercent)}%"
            } else {
                parPercentText = "N/A"
            }

            binding.parOrBetterPercent.text = parPercentText
        }

        return binding.root
    }

    private suspend fun calculateTotalScore(roundId: Long): Int {
        val holeStats = viewmodel.getRoundStatistics(roundId)

        holeStats.forEach { hole ->
            if (hole.holeScore == null) return -1
        }

        return holeStats.sumOf { it.holeScore!! }
    }

    private suspend fun calculateScoreToPar(roundId: Long, score: Int): String {
        val holeStats = viewmodel.getRoundStatistics(roundId)

        holeStats.forEach { hole ->
            if (hole.holePar == null) return "N/A"
        }

        val roundPar = holeStats.sumOf { it.holePar!! }

        return if (score == roundPar) {
            "E"
        } else if (score > roundPar) {
            "+${score-roundPar}"
        } else {
            //score < roundPar
            "-${roundPar-score}"
        }
    }

    private suspend fun calculateParOrBetterPercent() : Double {
        val rounds = viewmodel.getAllRounds()
        var parOrBetterCount = 0
        var totalHolesPlayed = 0

        for (round in rounds) {
            val holes = viewmodel.getRoundStatistics(round.id)

            for (hole in holes) {
                if (hole.holePar != null && hole.holeScore != null) {
                    //"played" hole
                    if (hole.holePar!! >= hole.holeScore!!) {
                        parOrBetterCount++
                    }
                    totalHolesPlayed++
                }
            }
        }

        return if (totalHolesPlayed > 0) (parOrBetterCount.toDouble() / totalHolesPlayed) * 100 else -1.0
    }
}