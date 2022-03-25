package com.tnote.tnoteapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.databinding.FragmentAccountBinding
import com.tnote.tnoteapp.logic.ApplicationViewModel
import com.tnote.tnoteapp.ui.ApplicationActivity
import com.tnote.tnoteapp.ui.MainActivity
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager


class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ApplicationViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ApplicationActivity).viewModel
        sessionManager = SessionManager(requireContext())

        viewModel.getCurrentUser(
            sessionManager.getUserId(),
            sessionManager.getAuthToken()
        )

        binding.btnLogout.setOnClickListener {
            viewModel.logout(sessionManager.getAuthToken())

            sessionManager.clearPrefs()

            startActivity(Intent(activity, MainActivity::class.java))
            (activity as ApplicationActivity).finish()
        }

        viewModel.accountFragmentState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    hideProgressBar()

                    it.data?.let { currentUser ->
                        binding.tvAccountName.text = currentUser.name
                        binding.tvAccountEmail.text = currentUser.email
                        binding.tvAccountCreatedAt.text = currentUser.created_at.substring(0, 10)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "Error fetching user data",
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

    private fun hideProgressBar() {
        binding.accountFragmentProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.accountFragmentProgressBar.visibility = View.VISIBLE
    }
}