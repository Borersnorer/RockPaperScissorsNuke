package com.example.rpsgame

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONObject

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

    private var activeItems = mutableListOf<String>()
    private var beatsMap = mutableMapOf<String, List<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvTotal = findViewById(R.id.tvTotal)
        tvStreak = findViewById(R.id.tvStreak)
        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)
        itemSelectionLayout = findViewById(R.id.itemSelectionLayout)
        btnStartRound = findViewById(R.id.btnStartRound)

        // Load persistent data
        val prefs = getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        totalScore = prefs.getInt("total_score", 0)
        streak = 0

        // Load owned items
        activeItems = prefs.getStringSet("owned_items", setOf("rock","paper","scissors"))?.toMutableList()
            ?: mutableListOf("rock","paper","scissors")

        // Load beats map
        val beatsJson = prefs.getString("beats_map", null)
        beatsMap = if (beatsJson != null) {
            jsonToMap(beatsJson)
        } else {
            mutableMapOf(
                "rock" to listOf("scissors"),
                "paper" to listOf("rock"),
                "scissors" to listOf("paper")
            )
        }

        updateScoreUI()
        populateItemSelection()

        btnStartRound.setOnClickListener {
            if (selectedItem == null) {
                Toast.makeText(this, "Select an item first!", Toast.LENGTH_SHORT).show()
            } else {
                startRound()
            }
        }
    }

    /** Create scrollable image selection **/
    private fun populateItemSelection() {
        itemSelectionLayout.removeAllViews()
        var lastClicked: ImageView? = null

        for (item in activeItems) {
            val imageView = ImageView(this).apply {
                val resId = resources.getIdentifier(item, "drawable", packageName)
                setImageResource(resId)
                val size = resources.getDimensionPixelSize(R.dimen.item_icon_size)
                layoutParams = LinearLayout.LayoutParams(size, size).apply { setMargins(16, 0, 16, 0) }
                setOnClickListener {
                    selectedItem = item
                    // Highlight selected
                    lastClicked?.background = null
                    background = ContextCompat.getDrawable(this@GameActivity, R.drawable.selected_border)
                    lastClicked = this
                }
            }
            itemSelectionLayout.addView(imageView)
        }

        selectedItem = activeItems.firstOrNull()
    }

    /** Start a round **/
    private fun startRound() {
        val playerChoice = selectedItem ?: return
        animateTop()
        animateBottom(playerChoice)

        handler.postDelayed({
            stopAnimations(playerChoice)
        }, 3000)
    }

    private fun animateTop() {
        topAnimationRunning = true
        val runnable = object : Runnable {
            override fun run() {
                if (topAnimationRunning) {
                    val randomItem = activeItems.random()
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

        // Random final top item
        val finalTop = activeItems.random()
        topImage.setImageResource(resources.getIdentifier(finalTop, "drawable", packageName))
        bottomImage.setImageResource(resources.getIdentifier(playerChoice, "drawable", packageName))

        checkWinner(playerChoice, finalTop)
    }

    /** Dynamic win check based on beatsMap **/
    private fun checkWinner(player: String, opponent: String) {
        val playerWins = beatsMap[player]?.contains(opponent) == true
        when {
            player == opponent -> {
                Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show()
                streak = 0
            }
            playerWins -> {
                streak++
                val pointsGained = 1 shl (streak - 1)
                totalScore += pointsGained
                saveTotalScore()
                Toast.makeText(this, "You win! +$pointsGained points", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "You lose!", Toast.LENGTH_SHORT).show()
                streak = 0
            }
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

    /** Utility: Convert JSON string to Map<String, List<String>> **/
    private fun jsonToMap(json: String): MutableMap<String, List<String>> {
        val map = mutableMapOf<String, List<String>>()
        val obj = JSONObject(json)
        for (key in obj.keys()) {
            val list = mutableListOf<String>()
            val jsonArray = obj.getJSONArray(key)
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            map[key] = list
        }
        return map
    }
}
