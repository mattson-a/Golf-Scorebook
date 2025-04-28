package edu.msoe.mattsona.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.databinding.FragmentViewStatsBinding

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

        return binding.root
    }
}