package edu.msoe.mattsona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.RoundListAdapter
import edu.msoe.mattsona.databinding.FragmentViewRoundsBinding
import kotlinx.coroutines.launch

class ViewRoundsFragment : Fragment() {
    private var _binding: FragmentViewRoundsBinding? = null
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
        _binding = FragmentViewRoundsBinding.inflate(inflater, container, false)
        binding.roundRecycler.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val rounds = viewmodel.getAllRounds()
            if (rounds.isEmpty()) {
                val text = "No Rounds Found."
                binding.textView.text = text
            }
            val courseNameMap = viewmodel.getAllCourses().associate {it.id to it.name}
            binding.roundRecycler.adapter = RoundListAdapter(rounds.sortedBy {it.date}, courseNameMap)
        }
    }
}