package com.example.mindmeld

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DisplaydreamRes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_displaydream_res)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dremblog)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up logo click listener to navigate .navigate1. page when click on logo
        val homeImageView = findViewById<ImageView>(R.id.btnhomeee)
        homeImageView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)
        }
            val logoImageView = findViewById<ImageView>(R.id.adddream)
            logoImageView.setOnClickListener {
                val intent = Intent(this, Dreamcatcher::class.java)
                startActivity(intent)
            }
        val imgView = findViewById<ImageView>(R.id.arrbck)
        imgView.setOnClickListener {
            val intent = Intent(this, homepage::class.java)
            startActivity(intent)

        }
    }
}