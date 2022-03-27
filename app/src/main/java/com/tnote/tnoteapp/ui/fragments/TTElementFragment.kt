package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.tnote.tnoteapp.TTElementFragmentArgs
import com.tnote.tnoteapp.databinding.FragmentTtelementBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.SessionManager

class TTElementFragment : Fragment() {
    private var _binding: FragmentTtelementBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

    val args: TTElementFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTtelementBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        sessionManager = SessionManager(requireContext())


        val selectedElementId = args.ttElementId


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}