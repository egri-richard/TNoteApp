package com.tnote.tnoteapp.ui

import android.content.Intent
import android.media.ResourceBusyException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.databinding.ActivityRegisterBinding
import com.tnote.tnoteapp.logic.RegisterViewModel
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = RegisterViewModel()
        sessionManager = SessionManager(this)

        binding.btnBackFromRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                binding.etRegisterName.text.toString(),
                binding.etRegisterEmail.text.toString(),
                binding.etRegisterPassword.text.toString()
            )
        }

        viewModel.registerActivityState.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        sessionManager.saveCredentials(
                            it.token,
                            it.user.id
                        )
                    }

                    startActivity(Intent(this, ApplicationActivity::class.java))
                    finish()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        binding.root,
                        "This email is already taken",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.registerProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.registerProgressBar.visibility = View.VISIBLE
    }
}