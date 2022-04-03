package com.tnote.tnoteapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.adapters.TimetablesAdapter
import com.tnote.tnoteapp.databinding.FragmentTimetableslistBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.Timetable
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class TimetablesListFragment: Fragment(R.layout.fragment_timetableslist) {
    private var _binding: FragmentTimetableslistBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var timetablesAdapter: TimetablesAdapter

    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimetableslistBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        setupRV()
        sessionManager = SessionManager(requireContext())
        getData()

        binding.btnAddNewTimetable.setOnClickListener {
            addNewTimetable()
        }

        timetablesAdapter.setOnItemClickListener {
            val timetableId = Bundle().apply {
                putInt("timetableId", it.id!!)
            }
            findNavController().navigate(
                R.id.action_timetablesListFragment_to_timetableFragment,
                timetableId
            )
        }

        timetablesAdapter.setOnItemLongClickListener {
            createDeleteDialog(it.id!!).show()
        }

        viewModel.timetablesListFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { list ->
                        timetablesAdapter.differ.submitList(list)
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

    private fun getData() {
        viewModel.getTimetables(
            sessionManager.getUserId(),
            sessionManager.getAuthToken()
        )
    }

    private fun setupRV() {
        timetablesAdapter = TimetablesAdapter()
        binding.rvTimetables.apply {
            adapter = timetablesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.timetablesListProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.timetablesListProgressBar.visibility = View.VISIBLE
    }

    private fun addNewTimetable() {
        val name = binding.etNewTimetableName.text.toString()
        viewModel.createTimetable(
            Timetable(null, sessionManager.getUserId(), name),
            sessionManager.getAuthToken()
        )

        Toast.makeText(
            requireContext(),
            "Timetable $name created",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun delete(id: Int) {
        viewModel.deleteTimetable(
            id,
            sessionManager.getAuthToken()
        )

        getData()
    }

    private fun createDeleteDialog(id: Int): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Do you want to delete this timetable?")
            .setPositiveButton("Yes") { _, _ ->
                delete(id)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .create()

        return dialog
    }
}