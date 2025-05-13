package edu.msoe.mattsona

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import edu.msoe.mattsona.databinding.ListItemHoleStatisticBinding
import edu.msoe.mattsona.entities.HoleStatistic

class HoleStatisticHolder(
    private val binding: ListItemHoleStatisticBinding,
    private val possiblePars: List<Int>
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(hole: HoleStatistic) {
        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_spinner_dropdown_item,
            possiblePars
        )
        binding.parDropdown.adapter = arrayAdapter

        binding.parDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hole.holePar = possiblePars[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //hole.holePar = null
            }
        }

        if (hole.holePar != null) {
            binding.parDropdown.setSelection(hole.holePar!!-3) //-3 because possiblePars array is { 3, 4, 5 } so 3-3 = index 0, etc.
        }

        val holeText = "Hole ${hole.holeNumber}: "
        binding.holeText.text = holeText

        if (hole.holeScore != null) {
            val scoreText = "${hole.holeScore!!}"
            binding.scoreInput.setText(scoreText)
        }

        binding.scoreInput.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                hole.holeScore = null
            } else {
                hole.holeScore = text.toString().toInt()
            }
        }
    }
}

class HoleStatisticListAdapter(
    private val holes: List<HoleStatistic>,
    private val possiblePars: List<Int>
) : RecyclerView.Adapter<HoleStatisticHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoleStatisticHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemHoleStatisticBinding.inflate(inflater, parent, false)
        return HoleStatisticHolder(binding, possiblePars)
    }

    override fun onBindViewHolder(holder: HoleStatisticHolder, position: Int) {
        val hole = holes[position]
        holder.bind(hole)
    }

    override fun getItemCount() = holes.size
}