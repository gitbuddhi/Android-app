package com.example.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mindmeld.R

class DestinationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination)  // This should now reference the correct layout file
    }
}
