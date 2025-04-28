package edu.msoe.mattsona.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import edu.msoe.mattsona.GolfRepository
import edu.msoe.mattsona.GolfViewModel
import edu.msoe.mattsona.databinding.FragmentNewRoundBinding
import edu.msoe.mattsona.entities.Round
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class NewRoundFragment : Fragment() {
    private var _binding: FragmentNewRoundBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewmodel: GolfViewModel by viewModels()
    private val repository = GolfRepository.get()
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRoundBinding.inflate(inflater, container, false)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        //set default value of date
        selectedDate = calendar.time
        binding.dateInput.setText(dateFormat.format(selectedDate!!))

        val dateSetListener = DatePickerDialog.OnDateSetListener {_, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            selectedDate = calendar.time

            binding.dateInput.setText(dateFormat.format(selectedDate!!))
        }

        binding.dateInput.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.createRoundButton.setOnClickListener {
            if (validateFields()) {
                var roundId: UUID? = null
                lifecycleScope.launch {
                    //save new round to db
                    if (!viewmodel.courseExists(binding.courseInput.text.toString())) {
                        //need to add course to db
                        viewmodel.createNewCourse(binding.courseInput.text.toString())
                    }
                    val courseId = repository.getCourseByName(binding.courseInput.text.toString()).id
                    roundId = viewmodel.createNewRound(courseId, selectedDate!!, getSelectedHoles())
                }
                //wait until roundId is populated
                while (roundId == null) {
                    continue
                }

                findNavController().navigate(NewRoundFragmentDirections.goToRoundScoring(roundId!!))
//                Snackbar.make(requireView(), "Round was added to the Database!", Snackbar.LENGTH_LONG).show()
//                findNavController().navigate(NewRoundFragmentDirections.returnToLanding())
            }
        }

        return binding.root
    }

    /**
     * Validates the input fields on this page to ensure all needed info is present
     */
    private fun validateFields() : Boolean {
        return (binding.courseInput.text.isNotEmpty() && binding.dateInput.text.isNotEmpty() &&
                binding.teesInput.text.isNotEmpty())
    }

    /**
     * Gets the selected number of holes as an Int from the holes toggle button
     */
    private fun getSelectedHoles() : Int {
        return if (binding.numHolesToggle.isChecked) {
            //toggle button is in state "on" -> secondary value -> 9 holes
            9
        } else {
            //toggle button is in state "off" -> default value -> 18 holes
            18
        }
    }
}