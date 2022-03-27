package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.adapters.TTElementsAdapter
import com.tnote.tnoteapp.databinding.FragmentTimetableBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class TimetableFragment: Fragment(R.layout.fragment_timetable) {
    private var _binding: FragmentTimetableBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var ttElementsAdapter: TTElementsAdapter

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
        setupRV()
        sessionManager = SessionManager(requireContext())

        val timetableId = args.timetableId

        viewModel.getSelectedTimetable(
            timetableId,
            sessionManager.getAuthToken()
        )

        ttElementsAdapter.setOnItemClickListener {
            val ttElementId = Bundle().apply {
                putInt("ttElementId", it.id)
            }
            findNavController().navigate(
                R.id.action_timetablesListFragment_to_timetableFragment,
                ttElementId
            )
        }

        viewModel.timetableFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { list ->
                        ttElementsAdapter.differ.submitList(list)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Unexpected Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupRV() {
        ttElementsAdapter = TTElementsAdapter()
        binding.rvTTElements.apply {
            adapter = ttElementsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.fragmentTimetableProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.fragmentTimetableProgressBar.visibility = View.VISIBLE
    }
}