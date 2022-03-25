package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.FragmentTimetableBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class TimetableFragment: Fragment(R.layout.fragment_timetable) {
    private var _binding: FragmentTimetableBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

    val args: TimetableFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        sessionManager = SessionManager(requireContext())

        val timetableId = args.timetableId

        viewModel.getSelectedTimetable(
            timetableId,
            sessionManager.getAuthToken()
        )

        viewModel.timetableFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {}
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}