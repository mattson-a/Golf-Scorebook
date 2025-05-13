package edu.msoe.mattsona

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.mattsona.databinding.ListItemRoundBinding
import edu.msoe.mattsona.entities.Round
import java.text.SimpleDateFormat
import java.util.Locale

class RoundHolder(
    private val binding: ListItemRoundBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(round: Round, courseName: String, onRoundClicked: (roundId: Long) -> Unit) {
        //if (round != null) {
        val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())

        val detailText = "$courseName | ${dateFormat.format(round.date)} | ${round.holes} Holes"
        binding.roundDetails.text = detailText

        binding.roundDetails.setOnClickListener {
            onRoundClicked(round.id)
        }
        /*} else {
            //no rounds, give appropriate message
            val detailText = "No Rounds Found. Create a New Round First!"
            binding.roundDetails.text = detailText
        }*/
    }
}

class RoundListAdapter(
    private val rounds: List<Round>,
    private val coursesMap: Map<Long, String>,
    private val onRoundClicked: (roundId: Long) -> Unit
) : RecyclerView.Adapter<RoundHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemRoundBinding.inflate(inflater, parent, false)
        return RoundHolder(binding)
    }

    override fun onBindViewHolder(holder: RoundHolder, position: Int) {
        if (rounds.isNotEmpty()) {
            val round = rounds[position]
            val course = coursesMap[round.courseId]
            holder.bind(round, course!!) { roundId ->
                onRoundClicked(roundId)
            }
        }/* else {
            //no rounds
            holder.bind(null, "")
        }*/
    }

    override fun getItemCount() = rounds.size
}