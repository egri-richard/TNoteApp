package com.tnote.tnoteapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.R
import com.tnote.tnoteapp.databinding.FragmentTtelementBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class TTElementFragment : Fragment() {
    private var _binding: FragmentTtelementBinding? = null
    val binding get() = _binding!!

    lateinit var viewModel: ApplicationViewModel
    lateinit var sessionManager: SessionManager

    val args: TTElementFragmentArgs by navArgs()

    lateinit var spAdapter: ArrayAdapter<String>
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
        setupSpAdapter()

        val selectedElementId = args.ttElementId

        viewModel.getSelectedTTELment(
            selectedElementId,
            sessionManager.getAuthToken()
        )


        viewModel.ttElementFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()
                    //TODO: fragment design + fill with data here
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Unexpected Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> showProgressBar()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun hideProgressBar() {
        binding.ttElementFragmentProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.ttElementFragmentProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpAdapter() {
        spAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spitem_day,
            resources.getStringArray(R.array.days_array))
    }
}