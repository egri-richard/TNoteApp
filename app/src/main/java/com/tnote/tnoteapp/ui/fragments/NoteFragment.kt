package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.FragmentNoteBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.ui.ApplicationActivity

class NoteFragment: Fragment(R.layout.fragment_note) {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    val args: NoteFragmentArgs by navArgs()

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

        val note: Note = args.note

        binding.etShownNoteTitle.setText(note.title)
        binding.etShownNoteContent.setText(note.content)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}