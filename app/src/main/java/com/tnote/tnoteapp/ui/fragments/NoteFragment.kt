package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.FragmentNoteBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.SessionManager

class NoteFragment: Fragment(R.layout.fragment_note) {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

    val args: NoteFragmentArgs by navArgs()
    lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        sessionManager = SessionManager(requireContext())

        note = args.note

        binding.etShownNoteTitle.setText(note.title)
        binding.etShownNoteContent.setText(note.content)

        binding.btnSaveShownNote.setOnClickListener {
            saveNoteChanges(note)
            Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun saveNoteChanges(note: Note) {
        note.title = binding.etShownNoteTitle.text.toString()
        note.content = binding.etShownNoteContent.text.toString()

        if (note.id == null) {
            viewModel.createNote(
                note,
                sessionManager.getAuthToken()
            )
        } else {
            viewModel.updateNote(
                note.id,
                sessionManager.getAuthToken(),
                note
            )
        }
    }
}