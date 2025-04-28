package edu.msoe.mattsona

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.mattsona.databinding.ListItemRoundBinding
import edu.msoe.mattsona.entities.Round
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class RoundHolder(
    private val binding: ListItemRoundBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(round: Round, courseName: String) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val detailText = "$courseName | ${dateFormat.format(round.date)} | ${round.holes} Holes"
        binding.roundDetails.text = detailText
    }
}

class RoundListAdapter(
    private val rounds: List<Round>,
    private val coursesMap: Map<UUID, String>
) : RecyclerView.Adapter<RoundHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemRoundBinding.inflate(inflater, parent, false)
        return RoundHolder(binding)
    }

    override fun onBindViewHolder(holder: RoundHolder, position: Int) {
        val round = rounds[position]
        val course = coursesMap[round.courseId]
        holder.bind(round, course!!)
    }

    override fun getItemCount() = rounds.size
}