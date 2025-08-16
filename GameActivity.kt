class GameActivity : AppCompatActivity() {

    private lateinit var topImage: ImageView
    private lateinit var bottomImage: ImageView
    private lateinit var btnRock: Button
    private lateinit var btnPaper: Button
    private lateinit var btnScissors: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        topImage = findViewById(R.id.topImage)
        bottomImage = findViewById(R.id.bottomImage)

        btnRock = findViewById(R.id.btnRock)
        btnPaper = findViewById(R.id.btnPaper)
        btnScissors = findViewById(R.id.btnScissors)

        btnRock.setOnClickListener { startRound("rock") }
        btnPaper.setOnClickListener { startRound("paper") }
        btnScissors.setOnClickListener { startRound("scissors") }
    }

    private fun startRound(playerChoice: String) {
        // TODO: animate both sides and stop after countdown
    }
}
