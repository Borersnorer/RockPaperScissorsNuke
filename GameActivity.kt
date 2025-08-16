package com.example.rpsgame

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var tvStreak: TextView
    private lateinit var topImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var itemSelectionLayout: LinearLayout
    private lateinit var btnStartRound: Button

    private val handler = Handler(Looper.getMainLooper())
    private var topAnimationRunning = false
    private var bottomAnimationRunning = false

    private var streak = 0
    private var totalScore = 0
    private var selectedItem: String? = null

    private val itemAnimations = listOf("rock", "paper", "scissors")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvTotal = findViewById(R.id.tvTotal)
        tvStreak = findViewById(R.id.tvStreak)
        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)
        itemSelectionLayout = findViewById(R.id.itemSelectionLayout)
        btnStartRound = findViewById(R.id.btnStartRound)

        // Load saved total score
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        totalScore = prefs.getInt("total_score", 0)
        streak = 0
        updateScoreUI()

        // Load owned items from prefs
        val ownedItems = loadOwnedItems()
        setupItemSelection(ownedItems)

        btnStartRound.setOnClickListener {
            if (selectedItem == null) {
                Toast.makeText(this, "Please select an item!", Toast.LENGTH_SHORT).show()
            } else {
                startRound()
            }
        }
    }

    private fun loadOwnedItems(): List<String> {
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        val defaultItems = setOf("rock", "paper", "scissors")
        val ownedSet = prefs.getStringSet("owned_items", defaultItems) ?: defaultItems
        return ownedSet.toList()
    }

    private fun setupItemSelection(items: List<String>) {
        itemSelectionLayout.removeAllViews()
        var lastClicked: ImageView? = null

        for (item in items) {
            val imageView = ImageView(this).apply {
                val resId = resources.getIdentifier(item, "drawable", packageName)
                setImageResource(resId)
                val size = resources.getDimensionPixelSize(R.dimen.item_icon_size)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    setMargins(16, 0, 16, 0)
                }
                setOnClickListener {
                    selectedItem = item
                    // Highlight selection
                    lastClicked?.background = null
                    background = ContextCompat.getDrawable(this@GameActivity, R.drawable.selected_border)
                    lastClicked = this
                }
            }
            itemSelectionLayout.addView(imageView)
        }
    }

    private fun startRound() {
        val playerChoice = selectedItem ?: return
        animateTop()
        animateBottom(playerChoice)

        handler.postDelayed({
            stopAnimations(playerChoice)
        }, 3000) // 3 second countdown
    }

    private fun animateTop() {
        topAnimationRunning = true
        val runnable = object : Runnable {
            override fun run() {
                if (topAnimationRunning) {
                    val randomItem = itemAnimations.random()
                    val resId = resources.getIdentifier(randomItem, "drawable", packageName)
                    topImage.setImageResource(resId)
                    handler.postDelayed(this, 300)
                }
            }
        }
        handler.post(runnable)
    }

    private fun animateBottom(playerChoice: String) {
        bottomAnimationRunning = true
        val resId = resources.getIdentifier(playerChoice, "drawable", packageName)
        val runnable = object : Runnable {
            override fun run() {
                if (bottomAnimationRunning) {
                    bottomImage.setImageResource(resId)
                    handler.postDelayed(this, 300)
                }
            }
        }
        handler.post(runnable)
    }

    private fun stopAnimations(playerChoice: String) {
        topAnimationRunning = false
        bottomAnimationRunning = false

        // Random final choice for top
        val finalTop = itemAnimations.random()
        val topResId = resources.getIdentifier(finalTop, "drawable", packageName)
        topImage.setImageResource(topResId)

        val bottomResId = resources.getIdentifier(playerChoice, "drawable", packageName)
        bottomImage.setImageResource(bottomResId)

        checkWinner(playerChoice, finalTop)
    }

    private fun checkWinner(player: String, cpu: String) {
        if (player == cpu) {
            Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show()
            streak = 0
        } else if (
            (player == "rock" && cpu == "scissors") ||
            (player == "paper" && cpu == "rock") ||
            (player == "scissors" && cpu == "paper")
        ) {
            streak++
            val points = if (streak == 1) 1 else (Math.pow(2.0, (streak - 1).toDouble())).toInt()
            totalScore += points
            saveTotalScore()
            Toast.makeText(this, "You win! +$points points", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
            streak = 0
        }
        updateScoreUI()
    }

    private fun saveTotalScore() {
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("total_score", totalScore).apply()
    }

    private fun updateScoreUI() {
        tvTotal.text = "Total: $totalScore"
        tvStreak.text = "Streak: $streak"
    }
}
