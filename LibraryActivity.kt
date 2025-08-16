package com.example.rockpaperscissors

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LibraryActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var btnUpgrade1: Button
    private lateinit var btnUpgrade2: Button
    private lateinit var btnUpgrade3: Button

    private var totalScore = 0
    private val prefs by lazy { getSharedPreferences("rps_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        tvTotal = findViewById(R.id.tvTotalLibrary)
        btnUpgrade1 = findViewById(R.id.btnUpgrade1)
        btnUpgrade2 = findViewById(R.id.btnUpgrade2)
        btnUpgrade3 = findViewById(R.id.btnUpgrade3)

        // Load permanent score
        totalScore = prefs.getInt("totalScore", 0)
        updateScoreUi()

        btnUpgrade1.setOnClickListener { spendPoints(10, "Upgrade 1") }
        btnUpgrade2.setOnClickListener { spendPoints(25, "Upgrade 2") }
        btnUpgrade3.setOnClickListener { spendPoints(50, "Upgrade 3") }
    }

    private fun spendPoints(cost: Int, upgradeName: String) {
        if (totalScore >= cost) {
            totalScore -= cost
            prefs.edit().putInt("totalScore", totalScore).apply()
            Toast.makeText(this, "$upgradeName purchased!", Toast.LENGTH_SHORT).show()
            updateScoreUi()
        } else {
            Toast.makeText(this, "Not enough points for $upgradeName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateScoreUi() {
        tvTotal.text = "Total Score: $totalScore"
    }
}
