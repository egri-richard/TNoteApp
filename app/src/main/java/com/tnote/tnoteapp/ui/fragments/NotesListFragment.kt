package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity

class NotesListFragment: Fragment(R.layout.fragment_noteslist) {
    lateinit var viewModel: ApplicationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel


    }
}