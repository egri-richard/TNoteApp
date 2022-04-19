package com.tnote.tnoteapp.ui

import android.content.Intent
import android.media.ResourceBusyException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
            val name = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val passwd = binding.etRegisterPassword.text.toString()

            if(name.isEmpty() || email.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Please fill in every field", Toast.LENGTH_SHORT).show()
            } else if (!email.contains('@') || !email.contains('.')) {
                Toast.makeText(this, "Please enter a correct email address", Toast.LENGTH_SHORT).show()
            } else if (passwd.length < 8) {
                Toast.makeText(this, "Password needs to be at least 8 characters", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(name, email, passwd)
            }
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