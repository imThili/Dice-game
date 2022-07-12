package se.umu.thek0034.thirty2point0

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val round = "Round"
    private val throwsLeft = "Throws Left"
    private val tapDiceCombine = "Tap the dice you want to combine"
    private val nextRound = "Next Round"
    private val option = "Option"
    private val tapDiceKeep = "Tap the dice you want to keep"
    private val keepAll = "Keep all and continue"
    private val totalSumText = "Total Sum:"

    private lateinit var optionButton: Button
    private lateinit var continueButton: Button
    private lateinit var throwButton: Button
    private lateinit var addCombinationButton: Button
    private lateinit var totalSumTextView: TextView
    private lateinit var throwsLeftTextView: TextView
    private lateinit var combinationsNotNeededTextView: TextView
    private lateinit var tapTheDiceTextView: TextView
    private lateinit var roundTextView: TextView
    private lateinit var optionsRadioButtonGroup: RadioGroup
    private lateinit var allDiceButtons: List<ImageButton>
    private lateinit var allOptionButtons: List<RadioButton>
    private lateinit var combinationsLayout: LinearLayout

    private var roundInt: Int = 1
    private val roundIntKey = "se.umu.thek0034.umu_thirty2point0.roundInt"
    private var throwsLeftInt: Int = 3
    private val throwsLeftKey = "se.umu.thek0034.umu_thirty2point0.throwsLeftInt"
    private var isAllDiceMarkedArray = BooleanArray(6)
    private val isAllDiceMarkedArrayKey = "se.umu.thek0034.thirty2point0.isAllDiceMarkedArray"
    private var optionInt = -1
    private val optionIntKey = "se.umu.thek0034.umu_thirty_test.checkedOption"
    private var faceOfAllDiceArray = IntArray(6)
    private val faceOfAllDiceArrayKey = "se.umu.thek0034.umu_thirty2point0.faceOfAllDice"
    private var totalSumInt: Int = 0
    private val totalSumIntKey = "se.umu.thek0034.umu_thirty2point0.totalSumInt"
    private var isInCombinationStage = false
    private val isInCombinationStageKey = "se.umu.thek0034.umu_thirty2point0.isInCombinationStage"
    private var allPointsArray = IntArray(10)
    private val allPointsArrayKey = "se.umu.thek0034.umu_thirty2point0.allPointsArray"
    private var isAllOptionsUsed = BooleanArray(10)
    private val isAllOptionUsedKey = "se.umu.thek0034.umu_thirty2point0.isAllOptionUsed"
    private var allDiceArray = arrayOfNulls<Dice>(6)
    private val allDiceArrayKey = "se.umu.thek0034.umu_thirty2point0.allDiceArray"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //This accesses all views in the activity
        findAllViewsByID()

        if (savedInstanceState == null) {
            //This sets the objects from the view classes that are supposed to be invisible to invisible when starting a new game.
            setInvisibilitiesOnNewGame()
        } else {

        }
        //This checks if the objects in the allDiceArray are null. If they are, 6 Dice objects are created and added to the allDiceArray.
        setArrayOfDice()
        //This includes all methods that handles the setOnClickListener methods.
        events()
    }

    private fun events() {
        //This marks or unmarks a dice.
        diceClickHandler()

        //This throws the dice that the user wants to throw and sets the correct visibilities and texts of the view classes depending on how many throws are left
        throwClickHandler()

        //If the arrow on the optionButton is pointing down, pressing it will open up all point system options for the user to choose from.
        //If the arrow is pointing up it will close all options.
        optionClickHandler()
        //If a radiobutton that represents an option in the point system is clicked on, the text on the optionButton will change the name of that option.
        optionsRadioButtonGroupClickHandler()

        //If the user still has throws left, pressing the continueButton will lead the user to the combination stage.
        //If the user is in the combinationStage, pressing the continueButton will lead the user to next round.
        continueClickHandler()

        //This adds the dice that the user has marked to a combination
        addCombinationClickHandler()
    }

    private fun addCombinationClickHandler() {
        addCombinationButton.setOnClickListener {
        for (dice in allDiceArray) {
            if (dice!!.isMarked()) {
                if (!dice.isRed()) {
                    Toast.makeText(
                        this,
                        "Please clear green before combining",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    var combinationList = mutableListOf<Dice>()
                    if (dice!!.isMarked()) {
                        combinationList.add(dice)
                    }
                    //This creates a horizontal layout which shows the combination the user has made
                    createCombinationHorizontalLayout(combinationList)
                }
            }
        }
        }
    }

    private fun createCombinationHorizontalLayout(combinationList: MutableList<Dice>) {

    }

    private fun continueClickHandler() {
        continueButton.setOnClickListener {
            if (!isInCombinationStage) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("You have $throwsLeftInt throws left, are you sure?")
                builder.setPositiveButton("Yes, continue anyways") { _, _ ->
                    throwsLeftInt = 0
                    updateThrowsLeftText()
                    handleViewClassesOnCombine()
                }
                setNegativeButtonAndShowAlert(builder)
            } else {
                if (optionButton.text == option) {
                    Toast.makeText(this, "You must choose an option", Toast.LENGTH_LONG).show()
                } else {
                    if (combinationsLayout.childCount == 0) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("You have not made any combinations, are you sure?")
                        builder.setPositiveButton("Yes, go to next round anyways") { _, _ ->

                            //This redirects the user to the result view, as all 10 rounds are finished.
                            if (roundInt != 11) {
//                                val intent = ResultActivity.newIntent(this@MainActivity, allPointsArray)
//                                startActivity(intent)
                            } else {
                                //This resets all view classes that aren't needed for the next round, and also starts a new round for the user.
                                goToNextRound()
                            }
                        }
                        setNegativeButtonAndShowAlert(builder)
                    }
                }
            }
        }
    }

    private fun goToNextRound() {
        TODO("Not yet implemented")
    }

    //This sets the negative button in an Alert to cancel and shows the dialog.
    private fun setNegativeButtonAndShowAlert(builder: AlertDialog.Builder) {
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }

    private fun optionsRadioButtonGroupClickHandler() {
        optionsRadioButtonGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            optionButton.text = radio.text

            if (isInCombinationStage && radio == allOptionButtons[0]) {
                combinationsNotNeededTextView.visibility = View.VISIBLE
            } else {
                combinationsNotNeededTextView.visibility = View.GONE
            }
        }
    }

    private fun optionClickHandler() {
        optionButton.setOnClickListener {
            if (optionsRadioButtonGroup.visibility == View.GONE) {
                optionsRadioButtonGroup.visibility = View.VISIBLE
                optionButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.expand_less,
                    0
                )
            } else {
                optionsRadioButtonGroup.visibility = View.GONE
                optionButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.expand_more,
                    0
                )
            }
        }
    }

    private fun throwClickHandler() {
        throwButton.setOnClickListener {
            when (throwsLeftInt) {
                //This sets the correct visibilities of the view classes after the first throw has been made
                3 -> setVisibilitiesAfterFirstThrow()
                //This sets the correct visibilities of the view classes after all 3 throws has been made
                //This also changes the text on the tapTheDiceTextView and the text on the continueButton
                //This also makes the dice non throwable and able to combine instead
                1 -> handleViewClassesOnCombine()
            }

            //This randomizes the face of all dice that the user wants to throw
            for (i in allDiceButtons.indices) {
                allDiceArray[i]!!.throwDice(allDiceButtons[i])
            }

            throwsLeftInt -= 1
            //This updates the text in the throwsLeftTextView that shows how many throws that are left
            updateThrowsLeftText()
        }
    }

    private fun updateThrowsLeftText() {
        val throwsLeft = "$throwsLeftInt $throwsLeft"
        throwsLeftTextView.text = throwsLeft
    }

    private fun handleViewClassesOnCombine() {
        addCombinationButton.visibility = View.VISIBLE
        throwButton.visibility = View.GONE
        isInCombinationStage = true
        tapTheDiceTextView.text = tapDiceCombine
        tapTheDiceTextView.setTextColor(Color.RED)

        if (roundInt == 10) {
            val end = "End"
            continueButton.text = end
        } else {
            continueButton.text = nextRound
        }

        for (dice in allDiceArray) {
            dice!!.setThrowable(false, allDiceButtons[allDiceArray.indexOf(dice)])
        }
    }

    private fun setVisibilitiesAfterFirstThrow() {
        optionButton.visibility = View.VISIBLE
        tapTheDiceTextView.visibility = View.VISIBLE
        continueButton.visibility = View.VISIBLE
        addCombinationButton.visibility = View.GONE
    }

    private fun diceClickHandler() {
        for (i in allDiceButtons.indices) {
            val button = allDiceButtons[i]
            val dice = allDiceArray[i]
            button.setOnClickListener {

                //This can make the dice green if the user wants to keep it when throwing other dice.
                //And this can make the dice red if the user is in the stage of making combinations.
                dice!!.clickHandler(button)
            }
        }
    }

    private fun setInvisibilitiesOnNewGame() {
        tapTheDiceTextView.visibility = View.GONE
        addCombinationButton.visibility = View.GONE
        combinationsNotNeededTextView.visibility = View.GONE
        optionButton.visibility = View.GONE
        continueButton.visibility = View.GONE
        optionsRadioButtonGroup.visibility = View.GONE
    }

    private fun setArrayOfDice() {
        if (allDiceArray[1] == null) {
            for (i in 0..5) {
                allDiceArray[i] = Dice(i)
            }
        }
    }

    private fun findAllViewsByID() {
        roundTextView = findViewById(R.id.round_text_view)
        throwsLeftTextView = findViewById(R.id.throws_left_text_view)
        allDiceButtons = listOf(
            findViewById(R.id.dice1),
            findViewById(R.id.dice2),
            findViewById(R.id.dice3),
            findViewById(R.id.dice4),
            findViewById(R.id.dice5),
            findViewById(R.id.dice6)
        )
        tapTheDiceTextView = findViewById(R.id.tap_the_dice_text_view)
        optionButton = findViewById(R.id.option_button)
        throwButton = findViewById(R.id.throw_button)
        addCombinationButton = findViewById(R.id.add_combination_button)
        combinationsNotNeededTextView = findViewById(R.id.combinations_not_needed_textView)
        optionsRadioButtonGroup = findViewById(R.id.radioGroup)
        allOptionButtons = listOf(
            findViewById(R.id.radioButton_low),
            findViewById(R.id.radioButton_4),
            findViewById(R.id.radioButton_5),
            findViewById(R.id.radioButton_6),
            findViewById(R.id.radioButton_7),
            findViewById(R.id.radioButton_8),
            findViewById(R.id.radioButton_9),
            findViewById(R.id.radioButton_10),
            findViewById(R.id.radioButton_11),
            findViewById(R.id.radioButton_12),
        )
        combinationsLayout = findViewById(R.id.combinations_layout)
        continueButton = findViewById(R.id.continue_button)
        totalSumTextView = findViewById(R.id.total_sum_text_view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(allDiceArrayKey, allDiceArray)
    }
}