package com.tnote.tnoteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tnote.tnoteapp.databinding.ActivityMainBinding
import com.tnote.tnoteapp.logic.MainActivityViewModel
import com.tnote.tnoteapp.util.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MainActivityViewModel()
        sessionManager = SessionManager(this)

        if (sessionManager.getAuthToken() != null) {
            startActivity(Intent(this, ApplicationActivity::class.java))
            finish()
        }

        binding.btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}