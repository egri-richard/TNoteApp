package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.models.Note
import com.tnote.tnoteapp.ui.ApplicationActivity

class NoteFragment: Fragment(R.layout.fragment_note) {
    lateinit var viewModel: ApplicationViewModel
    val args: NoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel

        val note: Note = arguments?.getBundle("note")
    }
}