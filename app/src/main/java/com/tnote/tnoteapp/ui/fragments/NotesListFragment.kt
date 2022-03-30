package com.tnote.tnoteapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.adapters.NotesAdapter
import com.tnote.tnoteapp.databinding.FragmentNoteslistBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class NotesListFragment: Fragment(R.layout.fragment_noteslist) {
    private var _binding: FragmentNoteslistBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

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
        sessionManager = SessionManager(requireContext())

        sessionManager.getUserId()
        Log.e("NotesListFragment", "SessionManager data: id: ${sessionManager.getUserId()} token: ${sessionManager.getAuthToken()}", )

        viewModel.getNotes(
            sessionManager.getUserId(),
            sessionManager.getAuthToken()
        )

        notesAdapter.setOnItemClickListener {
            Log.e("RvNotes", "Clicked", )

            val selectedNote = Bundle().apply {
                putSerializable("note", it)
            }
            findNavController().navigate(
                R.id.action_notesListFragment_to_noteFragment,
                selectedNote
            )
        }

        notesAdapter.setOnItemLongClickListener {
            createDeleteDialog(it.id!!).show()
        }

        binding.btnNewNote.setOnClickListener {
            val newNote = Note(
                "",
                null,
                null,
                sessionManager.getUserId(),
                "",
                null
            )

            val selectedNote = Bundle().apply {
                putSerializable("note", newNote)
            }
            findNavController().navigate(
                R.id.action_notesListFragment_to_noteFragment,
                selectedNote
            )
        }

        viewModel.notesListFragmentState.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let { notesListResponse ->
                        notesAdapter.differ.submitList(notesListResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("NotesListFragment", "Error occurred: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
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

    private fun delete(id: Int) {
        viewModel.deleteNote(
            id,
            sessionManager.getAuthToken()
        )

        viewModel.getNotes(
            sessionManager.getUserId(),
            sessionManager.getAuthToken()
        )
    }

    private fun createDeleteDialog(id: Int): AlertDialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                delete(id)
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .create()

        return dialog
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}