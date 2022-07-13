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
    private val combinationText = "Combination"
    private val removeText = "Remove"

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
    private var allCombinationsArray = arrayOfNulls<Array<Dice?>>(6)
    private var allDiceArray = arrayOfNulls<Dice>(6)

    private var roundInt: Int = 1
    private val roundIntKey = "se.umu.thek0034.umu_thirty2point0.roundInt"
    private var throwsLeftInt: Int = 3
    private val throwsLeftKey = "se.umu.thek0034.umu_thirty2point0.throwsLeftInt"
    private var totalSumInt: Int = 0
    private val totalSumIntKey = "se.umu.thek0034.umu_thirty2point0.totalSumInt"
    private var isInCombinationStage = false
    private val isInCombinationStageKey = "se.umu.thek0034.umu_thirty2point0.isInCombinationStage"
    private var isOptionRadioGroupOpen = false
    private val isOptionRadioGroupOpenKey = "se.umu.thek0034.umu_thirty2point0.isOptionRadioGroupOpen"
    private var currentOptionIndex = -1
    private val currentOptionIndexKey = "se.umu.thek0034.umu_thirty2point0.currentOptionIndex"
    private var allPointsArray = IntArray(10)
    private val allPointsArrayKey = "se.umu.thek0034.umu_thirty2point0.allPointsArray"
    private var areAllOptionsUsed = BooleanArray(10)
    private val areAllOptionsUsedKey = "se.umu.thek0034.umu_thirty2point0.isAllOptionUsed"

    private var diceOne = Dice(0)
    private val diceOneKey = "se.umu.thek0034.umu_thirty2point0.diceOne"
    private var diceTwo = Dice(1)
    private val diceTwoKey = "se.umu.thek0034.umu_thirty2point0.diceTwo"
    private var diceThree = Dice(2)
    private val diceThreeKey = "se.umu.thek0034.umu_thirty2point0.diceThree"
    private var diceFour = Dice(3)
    private val diceFourKey = "se.umu.thek0034.umu_thirty2point0.diceFour"
    private var diceFive = Dice(4)
    private val diceFiveKey = "se.umu.thek0034.umu_thirty2point0.diceFive"
    private var diceSix = Dice(5)
    private val diceSixKey = "se.umu.thek0034.umu_thirty2point0.diceSix"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //This accesses all views in the activity
        findAllViewsByID()

        if (savedInstanceState == null) {
            //This sets the objects from the view classes that are supposed to be invisible to invisible when starting a new game.
            setInvisibilitiesOnNewGame()
        } else {
            roundInt = savedInstanceState.getInt(roundIntKey)
            throwsLeftInt = savedInstanceState.getInt(throwsLeftKey)
            isInCombinationStage = savedInstanceState.getBoolean(isInCombinationStageKey)
            totalSumInt = savedInstanceState.getInt(totalSumIntKey)
            isOptionRadioGroupOpen = savedInstanceState.getBoolean(isOptionRadioGroupOpenKey)
            currentOptionIndex = savedInstanceState.getInt(currentOptionIndexKey)
            diceOne = savedInstanceState.getParcelable(diceOneKey)!!
            diceTwo = savedInstanceState.getParcelable(diceTwoKey)!!
            diceThree = savedInstanceState.getParcelable(diceThreeKey)!!
            diceFour = savedInstanceState.getParcelable(diceFourKey)!!
            diceFive = savedInstanceState.getParcelable(diceFiveKey)!!
            diceSix = savedInstanceState.getParcelable(diceSixKey)!!
            allPointsArray = savedInstanceState.getIntArray(allPointsArrayKey)!!
            areAllOptionsUsed = savedInstanceState.getBooleanArray(areAllOptionsUsedKey)!!

            setCorrectViewWithSavedInstanceStateVariables()
        }
        //This checks if the objects in the allDiceArray are null. If they are, 6 Dice objects are created and added to the allDiceArray.
        setArrayOfDice()
        //This includes all methods that handles the setOnClickListener methods.
        events()
    }

    private fun setCorrectViewWithSavedInstanceStateVariables() {
        updateRoundText()
        updateThrowsLeftText()
        updateTapTheDiceText()
        updateTotalSumText()
        //TODO: Fixa detta
//        for(dice in allDiceArray){
//            val diceButton = allDiceButtons[dice!!.getDiceNo()]
//            dice.setMarked(dice.isMarked(), diceButton)
//            if(!dice.isVisible()){
//                diceButton.visibility = View.GONE
//            }
//        }
        when(throwsLeftInt){
            3 -> setInvisibilitiesOnNewGame()
            2, 1 -> setVisibilitiesAfterFirstThrow()
            0 -> handleViewClassesOnCombine()
        }
        for(index in 0..5){
            if(areAllOptionsUsed[index]){
                allOptionButtons[index].isEnabled = false
            }
        }
        setOptionButtonText()

    }

    private fun setOptionButtonText() {
            if(currentOptionIndex == -1){
                optionButton.text = option
            }
            else{
                val text = allOptionButtons[currentOptionIndex].text
                optionButton.text = text
            }
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
            val combinationList = mutableListOf<Dice>()
            for (dice in allDiceArray) {
                if (dice!!.isMarked()) {
                    if (!dice.isRed()) {
                        Toast.makeText(
                            this, "Please clear green before combining", Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        if (dice.isMarked()) {
                            combinationList.add(dice)
                            dice.setMarked(false, allDiceButtons[dice.getDiceNo()])
                        }
                    }
                }
            }
            //This adds the combination the user has made to the view, and makes it possible to remove them as well.
            if (combinationList.size != 0) {
                addCombinationToView(combinationList)
            }
        }
    }

    private fun addCombinationToView(combinationList: MutableList<Dice>) {
        var sumOfDice = 0
        val combinationTextView = TextView(this)
        val sumOfDiceTextView = TextView(this)
        val horizontalCombinationLayout = LinearLayout(this)
        val removeButton = Button(this)

        //This returns the index of one of the 6 combination arrays that is empty
        val indexOfEmptyCombinationArray = getIndexOfEmptyCombinationArray()

        //This makes the dice that are in a combination invisible, and adds a image of those dice to a horizontal layout
        for (dice in combinationList) {
            addImageOfDiceToHorizontalLayout(
                dice,
                horizontalCombinationLayout,
                indexOfEmptyCombinationArray
            )
            sumOfDice += dice.getFace()
        }

        //This sets the design of the view classes in the combination that the user has created. Such as size and colour of the text.
        setDesignOfViewClassesInCombination(
            horizontalCombinationLayout,
            combinationTextView,
            sumOfDiceTextView,
            removeButton,
            sumOfDice
        )

        horizontalCombinationLayout.addView(sumOfDiceTextView)
        horizontalCombinationLayout.addView(removeButton)
        combinationsLayout.addView(combinationTextView)
        combinationsLayout.addView(horizontalCombinationLayout)

        //This removes a combination from the view when the removeButton on a combination is pressed
        removeClickHandler(
            removeButton,
            horizontalCombinationLayout,
            combinationTextView,
            combinationList,
            indexOfEmptyCombinationArray
        )
    }
    private fun updateTapTheDiceText(){
        if(isInCombinationStage) {
            tapTheDiceTextView.text = tapDiceCombine
            tapTheDiceTextView.setTextColor(Color.RED)
        }
        else{
            tapTheDiceTextView.text = tapDiceKeep
            tapTheDiceTextView.setTextColor(Color.parseColor("#689F38"))
        }
    }

    private fun addImageOfDiceToHorizontalLayout(
        dice: Dice,
        horizontalCombinationLayout: LinearLayout,
        indexOfEmptyCombinationArray: Int
    ) {

        val imageOfDice = ImageView(this)
        allDiceButtons[dice.getDiceNo()].visibility = View.GONE
        dice.setVisible(false)
        dice.setImageOfDiceForCombination(imageOfDice)
        horizontalCombinationLayout.addView(imageOfDice)

        for (i in 0..5) {
            if (allCombinationsArray[indexOfEmptyCombinationArray]!![i] == null) {
                allCombinationsArray[indexOfEmptyCombinationArray]!![i] = dice
                break
            }
        }
    }

    private fun removeClickHandler(
        removeButton: Button,
        horizontalCombinationLayout: LinearLayout,
        combinationTextView: TextView,
        combinationList: MutableList<Dice>,
        indexOfEmptyCombinationArray: Int
    ) {
        removeButton.setOnClickListener {
            combinationsLayout.removeView(horizontalCombinationLayout)
            combinationsLayout.removeView(combinationTextView)
            for (dice in allDiceArray) {
                if (combinationList.contains(dice)) {
                    dice!!.setVisible(true)
                    allDiceButtons[dice.getDiceNo()].visibility = View.VISIBLE
                }
            }
            for (index in allCombinationsArray[indexOfEmptyCombinationArray]!!.indices) {
                allCombinationsArray[indexOfEmptyCombinationArray]!![index] = null
            }
        }
    }

    private fun setDesignOfViewClassesInCombination(
        horizontalCombinationLayout: LinearLayout,
        combinationTextView: TextView,
        sumOfDiceTextView: TextView,
        removeButton: Button,
        sumOfDice: Int
    ) {
        horizontalCombinationLayout.orientation = LinearLayout.HORIZONTAL
        combinationTextView.setTextColor(Color.BLACK)
        combinationTextView.textSize = (25).toFloat()
        combinationTextView.text = combinationText
        sumOfDiceTextView.setTextColor(Color.BLACK)
        sumOfDiceTextView.textSize = (22).toFloat()
        removeButton.text = removeText
        val sumString = " = $sumOfDice"
        sumOfDiceTextView.text = sumString
    }

    private fun getIndexOfEmptyCombinationArray(): Int {
        var x = -1
        for (i in 0..5) {
            if (allCombinationsArray[i]!![0] == null) {
                x = i
                break
            }
        }
        return x
    }

    private fun startCombineBeforeThreeThrows() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You have $throwsLeftInt throws left, are you sure?")
        builder.setPositiveButton("Yes, continue anyways") { _, _ ->
            throwsLeftInt = 0
            updateThrowsLeftText()
            handleViewClassesOnCombine()
        }
        setNegativeButtonAndShowAlert(builder)
    }

    private fun continueClickHandler() {
        continueButton.setOnClickListener {
            if (!isInCombinationStage) {
                startCombineBeforeThreeThrows()
            } else {
                if (optionButton.text == option) {
                    Toast.makeText(this, "You must choose an option", Toast.LENGTH_SHORT).show()
                } else {
                    if (optionButton.text.equals("Low")) {
                        goToNextRoundWhenOptionEqualsLow()
                    } else if (combinationsLayout.childCount == 0) {
                        areYouSureAlertToGoToNextRoundWithoutPoints()
                    } else {
                        if (areAllCombinationsValid()) {
                            goToNextRoundOptionFourOrHigher()
                        }
                    }
                }
            }
        }
    }

    private fun goToNextRoundOptionFourOrHigher() {
        var numOfCombinations = 0
        for (index in 0..5) {
            if (allCombinationsArray[index]!![0] != null) {
                numOfCombinations++
            }
        }
        val optionIndex = optionButton.text.toString().toInt() - 3
        allPointsArray[optionIndex] =
            numOfCombinations * optionButton.text.toString().toInt()
        allOptionButtons[optionIndex].isEnabled = false
        goToNextRound()
    }

    private fun areAllCombinationsValid(): Boolean {
        for (i in 0..5) {
            if (allCombinationsArray[i]!![0] != null) {
                var sumOfCombination = 0
                for (dice in allCombinationsArray[i]!!) {
                    if (dice != null) {
                        sumOfCombination += dice.getFace()
                    }
                }
                val wantedSum = optionButton.text.toString().toInt()
                if (sumOfCombination != wantedSum) {
                    Toast.makeText(
                        this,
                        "One or more combinations do not have the correct sum",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
        }
        return true
    }

    private fun areYouSureAlertToGoToNextRoundWithoutPoints() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You have not made any combinations, are you sure?")
        builder.setPositiveButton("Yes, go to next round anyways") { _, _ ->
            val optionIndex = optionButton.text.toString().toInt() - 3
            allPointsArray[optionIndex] = 0
            allOptionButtons[optionIndex].isEnabled = false
            goToNextRound()
        }
        setNegativeButtonAndShowAlert(builder)
    }

    private fun goToNextRoundWhenOptionEqualsLow() {
        var sum = 0
        for (dice in allDiceArray) {
            if (dice!!.getFace() < 4) {
                sum += dice.getFace()
            }
        }
        allOptionButtons[0].isEnabled = false
        allPointsArray[0] = sum

        goToNextRound()
    }

    private fun goToNextRound() {
        for (dice in allDiceArray) {
            dice!!.resetDiceForNewTurn(allDiceButtons[dice.getDiceNo()])
        }
        throwsLeftInt = 3
        updateThrowsLeftText()
        updateTotalSumText()
        updateTapTheDiceText()
        for (combination in allCombinationsArray) {
            for (index in 0..5) {
                combination!![index] = null
            }
        }
        isInCombinationStage = false
        continueButton.text = keepAll
        roundInt++
        updateRoundText()
        setInvisibilitiesOnNewGame()
        combinationsLayout.removeAllViews()
        throwButton.visibility = View.VISIBLE
    }
    private fun updateRoundText(){
        val roundText = "$round $roundInt"
        roundTextView.text = roundText
    }

    private fun updateTotalSumText() {
        var sum = 0
        for (int in allPointsArray) {
            sum += int
        }
        totalSumInt = sum
        val text = "$totalSumText $totalSumInt"
        totalSumTextView.text = text
    }

    //This sets the negative button in an Alert to cancel and shows the dialog.
    private fun setNegativeButtonAndShowAlert(builder: AlertDialog.Builder) {
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN)
    }
    private fun setOptionRadioGroupVisibility(){
        if(isOptionRadioGroupOpen){
            optionsRadioButtonGroup.visibility = View.VISIBLE
        }
    }

    private fun setCombinationsNotNeededTextOnLowOption(){
        if (isInCombinationStage && currentOptionIndex == 0) {
            combinationsNotNeededTextView.visibility = View.VISIBLE
        } else {
            combinationsNotNeededTextView.visibility = View.GONE
        }
    }

    private fun optionsRadioButtonGroupClickHandler() {
        optionsRadioButtonGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            optionButton.text = radio.text
            currentOptionIndex = allOptionButtons.indexOf(radio)
            setCombinationsNotNeededTextOnLowOption()
        }
    }

    private fun optionClickHandler() {
        optionButton.setOnClickListener {
            if (optionsRadioButtonGroup.visibility == View.GONE) {
                isOptionRadioGroupOpen = true
                optionsRadioButtonGroup.visibility = View.VISIBLE
                optionButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.expand_less,
                    0
                )
            } else {
                optionsRadioButtonGroup.visibility = View.GONE
                isOptionRadioGroupOpen = false
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
        setOptionRadioGroupVisibility()
        addCombinationButton.visibility = View.VISIBLE
        continueButton.visibility = View.VISIBLE
        throwButton.visibility = View.GONE
        isInCombinationStage = true
        updateTapTheDiceText()

        if (roundInt == 10) {
            val end = "End"
            continueButton.text = end
        } else {
            continueButton.text = nextRound
        }

        for (dice in allDiceArray) {
            dice!!.setThrowable(false)
        }
    }

    private fun setVisibilitiesAfterFirstThrow() {
        setInvisibilitiesOnNewGame()
        continueButton.visibility = View.VISIBLE
        optionButton.visibility = View.VISIBLE
        tapTheDiceTextView.visibility = View.VISIBLE
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
        setOptionRadioGroupVisibility()
    }

    private fun setArrayOfDice() {
        allDiceArray[0] = diceOne
        allDiceArray[1] = diceTwo
        allDiceArray[2] = diceThree
        allDiceArray[3] = diceFour
        allDiceArray[4] = diceFive
        allDiceArray[5] = diceSix

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
    private fun getWhichOptionsThatHaveBeenUsed():BooleanArray{
        for(option in allOptionButtons){
            if(!option.isEnabled){
                areAllOptionsUsed[allOptionButtons.indexOf(option)] = true
            }
        }
        return areAllOptionsUsed
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(roundIntKey, roundInt)
        outState.putInt(throwsLeftKey, throwsLeftInt)
        outState.putBoolean(isInCombinationStageKey, isInCombinationStage)
        outState.putInt(totalSumIntKey, totalSumInt)
        outState.putInt(currentOptionIndexKey, currentOptionIndex)
        outState.putBoolean(isOptionRadioGroupOpenKey, isOptionRadioGroupOpen)
        outState.putIntArray(allPointsArrayKey, allPointsArray)
        outState.putBooleanArray(areAllOptionsUsedKey, getWhichOptionsThatHaveBeenUsed())
        outState.putParcelable(diceOneKey, diceOne)
        outState.putParcelable(diceTwoKey, diceTwo)
        outState.putParcelable(diceThreeKey, diceThree)
        outState.putParcelable(diceFourKey, diceFour)
        outState.putParcelable(diceFiveKey, diceFive)
        outState.putParcelable(diceSixKey, diceSix)
    }
}