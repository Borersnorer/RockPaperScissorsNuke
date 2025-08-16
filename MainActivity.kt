class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnPlay).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        findViewById<Button>(R.id.btnLibrary).setOnClickListener {
            // TODO: later open a library screen
            Toast.makeText(this, "Library not ready yet", Toast.LENGTH_SHORT).show()
        }
    }
}
