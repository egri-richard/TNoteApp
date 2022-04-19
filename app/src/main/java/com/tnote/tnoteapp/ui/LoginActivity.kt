package com.tnote.tnoteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.tnote.tnoteapp.databinding.ActivityLoginBinding
import com.tnote.tnoteapp.logic.LoginViewModel
import com.tnote.tnoteapp.util.Resource
import com.tnote.tnoteapp.util.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        loginViewModel = LoginViewModel()

        binding.btnBackFromLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val passwd = binding.etPassword.text.toString()

            if(email.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Please fill in every field", Toast.LENGTH_SHORT).show()
            } else if (!email.contains('@') || !email.contains('.')) {
                Toast.makeText(this, "Please enter a correct email address", Toast.LENGTH_SHORT).show()
            } else if (passwd.length < 8) {
                Toast.makeText(this, "Password needs to be at least 8 characters", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, passwd)
            }


        }

        loginViewModel.loginActivityState.observe(this) { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()

                    response.data?.let {
                        Log.e("LoginActivity", "Response: $it", )
                        Log.e("LoginActivity", "Response Id: ${it.user.id}", )
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
                        "Wrong Credentials",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    response.data.let {
                        Log.e("LoginActivity", "Response: $it", )
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.loginProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.loginProgressBar.visibility = View.VISIBLE
    }
}