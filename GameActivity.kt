package com.example.rockpaperscissors

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    // UI
    private lateinit var topImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var tvTotal: TextView
    private lateinit var tvStreak: TextView
    private lateinit var itemSelectionLayout: LinearLayout

    // Game
    private var running = false
    private var currentStreak = 0
    private var totalScore = 0
    private var activeItems = mutableListOf("rock", "paper", "scissors")
    private var beatsMap = mutableMapOf<String, List<String>>()
    private var selectedItem = "rock"

    // Storage
    private val prefs by lazy { getSharedPreferences("rps_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Link views
        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)
        tvTotal = findViewById(R.id.tvTotal)
        tvStreak = findViewById(R.id.tvStreak)
        itemSelectionLayout = findViewById(R.id.itemSelectionLayout)

        // Load persistent data
        totalScore = prefs.getInt("totalScore", 0)
        currentStreak = 0
        activeItems = prefs.getStringSet("activeItems", setOf("rock","paper","scissors"))?.toMutableList()
            ?: mutableListOf("rock", "paper", "scissors")

        val beatsJson = prefs.getString("beatsMap", null)
        beatsMap = if (beatsJson != null) {
            Utils.jsonToMap(beatsJson)
        } else {
            mutableMapOf(
                "rock" to listOf("scissors"),
                "paper" to listOf("rock"),
                "scissors" to listOf("paper")
            )
        }

        updateScoreUi()
        populateItemSelection()

    }

    /** Create scrollable buttons for each active item **/
    private fun populateItemSelection() {
        itemSelectionLayout.removeAllViews()
        for (item in activeItems) {
            val button = Button(this)
            button.text = item.capitalize()
            button.setOnClickListener { selectedItem = item }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 6, 0, 6)
            button.layoutParams = params
            itemSelectionLayout.addView(button)
        }
        selectedItem = activeItems.first() // default selection
    }

    /** Start a round using the chosen item **/
    fun startRound(view: android.view.View) {
        if (running) return
        running = true

        var topResult = ""

        val timer = object : CountDownTimer(3000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                val randomChoice = activeItems.random()
                topResult = randomChoice
                topImage.setImageResource(getDrawableForChoice(randomChoice))
                bottomImage.setImageResource(getDrawableForChoice(selectedItem))
            }

            override fun onFinish() {
                running = false
                val outcomeMessage: String
                when {
                    selectedItem == topResult -> {
                        currentStreak = 0
                        outcomeMessage = "DRAW! No points."
                    }
                    didPlayerWin(selectedItem, topResult) -> {
                        currentStreak++
                        val pointsGained = 1 shl (currentStreak - 1)
                        totalScore += pointsGained
                        prefs.edit().putInt("totalScore", totalScore).apply()
                        outcomeMessage = "You WIN! +$pointsGained points"
                    }
                    else -> {
                        currentStreak = 0
                        outcomeMessage = "You LOSE! Streak reset."
                    }
                }
                updateScoreUi()
                Toast.makeText(this@GameActivity, "$outcomeMessage\nTotal: $totalScore", Toast.LENGTH_LONG).show()
            }
        }
        timer.start()
    }

    /** Dynamic win check based on beatsMap **/
    private fun didPlayerWin(player: String, opponent: String): Boolean {
        return beatsMap[player]?.contains(opponent) == true
    }

    /** Map item name to drawable resource **/
    private fun getDrawableForChoice(choice: String): Int {
        return when (choice) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            "scissors" -> R.drawable.scissors
            else -> resources.getIdentifier(choice.lowercase(), "drawable", packageName)
        }
    }

    private fun updateScoreUi() {
        tvTotal.text = "Total: $totalScore"
        tvStreak.text = "Streak: $currentStreak"
    }
}
