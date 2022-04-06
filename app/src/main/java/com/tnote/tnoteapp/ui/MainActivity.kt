package com.tnote.tnoteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tnote.tnoteapp.databinding.ActivityMainBinding
import com.tnote.tnoteapp.util.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(applicationContext)

        Log.e("MainActivty", "SessionManager data: id: ${sessionManager.getUserId()} token: ${sessionManager.getAuthToken()}", )
        isLoggedIn(sessionManager.getUserId())


        binding.btnToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun isLoggedIn(userId: Int) {
        if (userId > 0) {
            startActivity(Intent(this, ApplicationActivity::class.java))
            finish()
        }
    }
}