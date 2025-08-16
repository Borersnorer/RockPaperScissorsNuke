package com.example.rockpaperscissors

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnLibrary: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link buttons from layout
        btnPlay = findViewById(R.id.btnPlay)
        btnLibrary = findViewById(R.id.btnLibrary)

        // Open GameActivity when Play is pressed
        btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        // Open LibraryActivity when Library is pressed
        btnLibrary.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }
    }
}
