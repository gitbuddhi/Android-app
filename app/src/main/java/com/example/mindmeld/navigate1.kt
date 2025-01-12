package com.example.mindmeld

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class navigate1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigate1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dremblog)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set up click listener to navigate .navigate2. page when click on > button
        val nextImageView = findViewById<ImageView>(R.id.nextarr)
        nextImageView.setOnClickListener {
            val intent = Intent(this,navigate2::class.java)
            startActivity(intent)
        }
    }
}