package se.umu.thek0034.thirty2point0

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val extraAllPointsArray = "se.umu.thek0034.umu_thirty_test.extraAllPointsArray"

//This class shows the result of the game. The user can also choose to play again here.
class ResultActivity : AppCompatActivity() {
    private val allOptionsArray = arrayOf("Low", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    private var text = ""
    private val textkey = "se.umu.thek0034.umu_thirty2point0.text"
    private var totalSumInt = 0
    private val totalSumIntKey = "se.umu.thek0034.umu_thirty2point0.totalSumInt"
    private val totalSumText = "Total Sum: "
    private lateinit var againButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val allPointsTextView = findViewById<TextView>(R.id.allPointsTextView)
        againButton = findViewById(R.id.againButton)
        val totalSumTextView = findViewById<TextView>(R.id.totalSum)

        if (savedInstanceState == null) {
            val allPointsArray = intent.getIntArrayExtra(extraAllPointsArray)

            //This sets the option name to the left and the points the user got for that option on the right side, for all options in the game. This also calculates the total sum of all points.
            setTextOfResultAndCalculateTotalSum(allPointsArray!!)

        } else {
            text = savedInstanceState.getString(textkey).toString()
            totalSumInt = savedInstanceState.getInt(totalSumIntKey)
        }

        //This handles what will happen when the againButton is pressed (a new game will start).
        againClickHandler()

        //This updates the text in the totalSumTextView.
        updateTotalSumText(totalSumTextView)

        //This sets the text in allPointsTextView to the string that included all options and their points
        allPointsTextView.text = text
    }

    private fun setTextOfResultAndCalculateTotalSum(allPointsArray: IntArray) {
        for (i in allPointsArray.indices) {
            text += allOptionsArray[i] + ": " + allPointsArray[i] + "\n"
            totalSumInt += allPointsArray[i]
        }
    }

    private fun updateTotalSumText(totalSumTextView: TextView) {
        val totalSumTextText = totalSumText + totalSumInt
        totalSumTextView.text = totalSumTextText
    }

    private fun againClickHandler() {
        againButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun newIntent(packageContext: Context, allPointsArray: IntArray): Intent {
            return Intent(packageContext, ResultActivity::class.java).apply {
                putExtra(extraAllPointsArray, allPointsArray)
            }
        }
    }

    //When the user presses the back button, this will show a toast that says the user can't. And nothing will happen.
    override fun onBackPressed() {
        Toast.makeText(
            this, "No cheating, you can't go back in this game!", Toast.LENGTH_SHORT
        )
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(textkey, text)
        outState.putInt(totalSumIntKey, totalSumInt)
    }
}