package com.tnote.tnoteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tnote.tnoteapp.databinding.ActivityApplicationBinding

class ApplicationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}