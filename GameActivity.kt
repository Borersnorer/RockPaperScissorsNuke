package com.example.rockpaperscissors

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    // UI
    private lateinit var topImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var btnRock: Button
    private lateinit var btnPaper: Button
    private lateinit var btnScissors: Button
    private lateinit var tvTotal: TextView
    private lateinit var tvStreak: TextView

    // Game
    private val choices = listOf("rock", "paper", "scissors")
    private var running = false
    private var currentStreak = 0             // resets on draw/lose and on activity start
    private var totalScore = 0               // persists forever

    // Storage for total score
    private val prefs by lazy { getSharedPreferences("rps_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Link views
        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)
        btnRock = findViewById(R.id.btnRock)
        btnPaper = findViewById(R.id.btnPaper)
        btnScissors = findViewById(R.id.btnScissors)
        tvTotal = findViewById(R.id.tvTotal)
        tvStreak = findViewById(R.id.tvStreak)

        // Load permanent total score; streak starts fresh at 0
        totalScore = prefs.getInt("totalScore", 0)
        currentStreak = 0
        updateScoreUi()

        // Listeners
        btnRock.setOnClickListener { startRound("rock") }
        btnPaper.setOnClickListener { startRound("paper") }
        btnScissors.setOnClickListener { startRound("scissors") }
    }

    private fun startRound(playerChoice: String) {
        if (running) return
        running = true

        var topResult = "" // what the top (opponent) ends on

        // 3 seconds total, swaps every 300ms
        val timer = object : CountDownTimer(3000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                // Alternate the top randomly
                val randomChoice = choices.random()
                topResult = randomChoice
                topImage.setImageResource(getDrawableForChoice(randomChoice))

                // Bottom shows the player's chosen item (you can change this to "mirror" if you want)
                bottomImage.setImageResource(getDrawableForChoice(playerChoice))
            }

            override fun onFinish() {
                running = false

                val outcomeMessage: String
                when {
                    playerChoice == topResult -> {
                        // Draw ends the streak, no points added
                        currentStreak = 0
                        outcomeMessage = "DRAW! No points."
                    }
                    didPlayerWin(playerChoice, topResult) -> {
                        // Win: increment streak, award 1,2,4,8,... points
                        currentStreak++

                        // 1 << (streak-1) gives 1,2,4,8...
                        val pointsGained = 1 shl (currentStreak - 1)
                        totalScore += pointsGained

                        // Persist only the total score
                        prefs.edit().putInt("totalScore", totalScore).apply()

                        outcomeMessage = "You WIN! +$pointsGained points"
                    }
                    else -> {
                        // Lose: streak resets, no points added
                        currentStreak = 0
                        outcomeMessage = "You LOSE! Streak reset."
                    }
                }

                updateScoreUi()
                Toast.makeText(
                    this@GameActivity,
                    "$outcomeMessage\nTotal: $totalScore",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        timer.start()
    }

    private fun didPlayerWin(player: String, opponent: String): Boolean {
        return (player == "rock" && opponent == "scissors") ||
               (player == "paper" && opponent == "rock") ||
               (player == "scissors" && opponent == "paper")
    }

    private fun getDrawableForChoice(choice: String): Int {
        return when (choice) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            "scissors" -> R.drawable.scissors
            else -> R.drawable.ic_launcher_foreground
        }
    }

    private fun updateScoreUi() {
        tvTotal.text = "Total: $totalScore"
        tvStreak.text = "Streak: $currentStreak"
    }
}
