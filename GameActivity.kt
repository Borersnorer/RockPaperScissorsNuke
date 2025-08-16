package com.example.rockpaperscissors

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var topImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var btnRock: Button
    private lateinit var btnPaper: Button
    private lateinit var btnScissors: Button

    private val choices = listOf("rock", "paper", "scissors")
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // link views
        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)
        btnRock = findViewById(R.id.btnRock)
        btnPaper = findViewById(R.id.btnPaper)
        btnScissors = findViewById(R.id.btnScissors)

        // set click listeners
        btnRock.setOnClickListener { startRound("rock") }
        btnPaper.setOnClickListener { startRound("paper") }
        btnScissors.setOnClickListener { startRound("scissors") }
    }

    private fun startRound(playerChoice: String) {
        if (running) return // don’t start another round if one is already running
        running = true

        var topResult = ""

        val timer = object : CountDownTimer(3000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                // random top choice
                val randomChoice = choices.random()
                topResult = randomChoice
                topImage.setImageResource(getDrawableForChoice(randomChoice))

                // bottom always shows player’s choice during animation
                bottomImage.setImageResource(getDrawableForChoice(playerChoice))
            }

            override fun onFinish() {
                running = false
                // final result message
                Toast.makeText(
                    this@GameActivity,
                    "Result: Top = $topResult, You = $playerChoice",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        timer.start()
    }

    private fun getDrawableForChoice(choice: String): Int {
        return when (choice) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            "scissors" -> R.drawable.scissors
            else -> R.drawable.ic_launcher_foreground
        }
    }
}

