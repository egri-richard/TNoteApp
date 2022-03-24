package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.adapters.NotesAdapter
import com.tnote.tnoteapp.databinding.FragmentNoteslistBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource

class NotesListFragment: Fragment(R.layout.fragment_noteslist) {
    private var _binding: FragmentNoteslistBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteslistBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        setupRV()

        notesAdapter.setOnItemClickListener {
            val selectedNote = Bundle().apply {
                putSerializable("note", it)
            }
            findNavController().navigate(
                R.id.action_notesListFragment_to_noteFragment,
                selectedNote
            )
        }

        viewModel.notesListFragmentState.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { notesListResponse ->
                        notesAdapter.differ.submitList(notesListResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("NotesFragment", "Error occured: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRV() {
        notesAdapter = NotesAdapter()
        binding.rvNotes.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.notesListProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.notesListProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}