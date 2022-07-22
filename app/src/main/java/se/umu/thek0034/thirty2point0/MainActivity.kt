package se.umu.thek0034.thirty2point0

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

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
    private val end = "End"

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
    private lateinit var allDiceButtonsList: List<ImageButton>
    private lateinit var allOptionButtonsList: List<RadioButton>
    private lateinit var combinationsLayout: LinearLayout
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
    private val isOptionRadioGroupOpenKey =
        "se.umu.thek0034.umu_thirty2point0.isOptionRadioGroupOpen"
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

        //This accesses all views from activity_main.xml in the activity
        findAllViewsByID()

        //This adds the 6 Dice objects instantiated above to the allDiceArray.
        //The Dice class stores information of a dice. Attributes: diceNo, face, isMarked, isThrowable, isRed, isVisible
        setArrayOfDice()

        if (savedInstanceState == null) {

            //This sets the objects from the view classes that are supposed to be invisible to invisible when starting a new game. And disables the dice ImageButtons.
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

            //This sets the correct view depending on the state of the variables, such as visibility of objects, text on buttons, etc.
            setCorrectViewWithSavedInstanceStateVariables()
        }
        //This includes all methods that handles the setOnClickListener methods (except for the button that removes a combination). See next method.
        events()
    }

    private fun events() {
        //This marks or unmarks a dice.
        diceClickHandler()

        //This throws the dice that the user wants to throw, and sets the correct visibilities and texts of the view classes depending on how many throws are left.
        throwClickHandler()

        //If the arrow on the optionButton is pointing down, pressing it will open up all point system options for the user to choose from.
        //If the arrow is pointing up, pressing it will close all options.
        optionClickHandler()

        //If a radiobutton that represents an option in the point system is clicked on, the text on the optionButton will change to the name of that option.
        optionsRadioButtonGroupClickHandler()

        //This changes the view for the user, depending on if the user wants to go to the combining stage of a round, next round, or to the result activity.
        continueClickHandler()

        //This adds the dice that the user has marked to a combination
        addCombinationClickHandler()
    }

    //This sets the correct view depending on the state of the variables, such as visibility of objects, text on buttons, etc.
    private fun setCorrectViewWithSavedInstanceStateVariables() {
        setArrayOfDice()
        updateRoundText()
        updateThrowsLeftText()
        updateTapTheDiceText()
        updateTotalSumText()
        setOptionRadioGroupVisibility()
        setVisibilityOfCombinationsNotNeededText()
        setOptionButtonText()
        createCombinationsFromDiceCombinationNumbers()

        when (throwsLeftInt) {
            3 -> setInvisibilitiesOnNewGame()
            2, 1 -> setVisibilitiesAfterFirstThrow()
            0 -> handleViewClassesOnCombine()
        }
        for (index in 0..9) {
            if (areAllOptionsUsed[index]) {
                allOptionButtonsList[index].isEnabled = false
            }
        }
    }

    //This recreates the combinations a user has made from the attribute "combinationNumber" that the dice have.
    private fun createCombinationsFromDiceCombinationNumbers() {
        val mutableLists = mutableListOf<MutableList<Dice>>(
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
        )

        for (dice in allDiceArray) {
            val diceButton = allDiceButtonsList[dice!!.getDiceNo()]
            dice.setMarked(dice.isMarked(), diceButton)
            if (!dice.isVisible()) {
                diceButton.visibility = View.GONE
            }
            when (dice.getCombinationNumber()) {
                1 -> mutableLists[0].add(dice)
                2 -> mutableLists[1].add(dice)
                3 -> mutableLists[2].add(dice)
                4 -> mutableLists[3].add(dice)
                5 -> mutableLists[4].add(dice)
                6 -> mutableLists[5].add(dice)
            }
        }
        for (list in mutableLists) {
            if (list.isNotEmpty()) {
                addCombinationToView(list)
            }
        }
    }

    //This handles what happens when the "Add combination - button" is pressed.
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
                            dice.setMarked(false, allDiceButtonsList[dice.getDiceNo()])
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

    //This adds the combination the user has made to the view.
    private fun addCombinationToView(combinationList: MutableList<Dice>) {
        var sumOfDice = 0
        val combinationTextView = TextView(this)
        val sumOfDiceTextView = TextView(this)
        val horizontalCombinationLayout = LinearLayout(this)
        val removeButton = Button(this)

        //This returns the index of one of the 6 combination arrays that is empty
        val intOfEmptyCombination = getIntOfEmptyCombinationNumber()

        //This makes the dice that are in a combination invisible, and adds a image of those dice to a horizontal layout
        for (dice in combinationList) {
            addImageOfDiceToHorizontalLayout(
                dice,
                horizontalCombinationLayout
            )
            dice.setCombinationNumber(intOfEmptyCombination)
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
        )
    }

    //When a user makes a combination of dice, this will create a horizontal layout with imageViews that shows which dice a combination contains.
    private fun addImageOfDiceToHorizontalLayout(
        dice: Dice,
        horizontalCombinationLayout: LinearLayout
    ) {
        val imageOfDice = ImageView(this)
        allDiceButtonsList[dice.getDiceNo()].visibility = View.GONE
        dice.setVisible(false)
        dice.setImageOfDiceForCombination(imageOfDice)
        horizontalCombinationLayout.addView(imageOfDice)
    }

    //This removes a combination that a user has made.
    private fun removeClickHandler(
        removeButton: Button,
        horizontalCombinationLayout: LinearLayout,
        combinationTextView: TextView,
        combinationList: MutableList<Dice>,
    ) {
        removeButton.setOnClickListener {
            combinationsLayout.removeView(horizontalCombinationLayout)
            combinationsLayout.removeView(combinationTextView)
            for (dice in allDiceArray) {
                if (combinationList.contains(dice)) {
                    dice!!.setVisible(true)
                    allDiceButtonsList[dice.getDiceNo()].visibility = View.VISIBLE
                }
            }
            for (combinationDice in combinationList) {
                for (dice in allDiceArray) {
                    if (combinationDice.getDiceNo() == dice!!.getDiceNo()) {
                        dice.setCombinationNumber(0)
                    }
                }
            }
        }
    }

    //This sets the design such as text size and text color of some of the objects inside a combination
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

    //If a combination the user has made contains a dice, the dice attribute "combinationNumber" will be between 1-6.
    //This will return an integer that no dice has as combination number, for when the user makes a new combination.
    private fun getIntOfEmptyCombinationNumber(): Int {
        val intList = mutableListOf(0, 0, 0, 0, 0, 0)

        for (dice in allDiceArray) {
            val emptyCombination = dice!!.getCombinationNumber()
            if (emptyCombination != 1) {
                intList[0]++
            }
            if (emptyCombination != 2) {
                intList[1]++
            }
            if (emptyCombination != 3) {
                intList[2]++
            }
            if (emptyCombination != 4) {
                intList[3]++
            }
            if (emptyCombination != 5) {
                intList[4]++
            }
            if (emptyCombination != 6) {
                intList[5]++
            }
        }
        for (int in intList) {
            if (int == 6) {
                return intList.indexOf(int) + 1
            }
        }
        return 0
    }

    //This shows an alert that asks the user if they want to continue without using all 3 throws.
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

    //If the user still has throws left, pressing the continueButton will lead the user to the combination stage.
    //If the user is in the combinationStage, pressing the continueButton will lead the user to next round,
    //or if the user has finished all 10 rounds, pressing the continuebutton will lead the user to the result activity.
    //This also makes sure that the user cannot do anything that is not OK according to the rules of the game
    private fun continueClickHandler() {
        continueButton.setOnClickListener {
            if (!isInCombinationStage) {
                startCombineBeforeThreeThrows()
            } else {

                //The user can't continue to next round without choosing an option
                if (optionButton.text == option) {
                    Toast.makeText(this, "You must choose an option", Toast.LENGTH_SHORT).show()

                    //The user can't continue to next round if they choose an option that they have already used
                } else if (!allOptionButtonsList[currentOptionIndex].isEnabled) {
                    Toast.makeText(this, "Option already used, choose another", Toast.LENGTH_SHORT)
                        .show()

                } else {

                    //This calculates the user's points this round when the option they choose is low, and takes the user to next round (or result activity)
                    if (optionButton.text.equals("Low")) {
                        calculatePointsAndGoToNextRoundWhenOptionEqualsLow()

                        //This brings up an alert asking the user if they really want to continue even though they haven't made any combinations. If YES, it will take the user to the next round (or result activity)
                    } else if (combinationsLayout.childCount == 0) {
                        areYouSureAlertToGoToNextRoundWithoutPoints()

                        //This calculates the user's points this round when the option they choose is 4-12, and takes the user to next round (or result activity)
                    } else {
                        if (areAllCombinationsValid()) {
                            calculatePointsAndGoToNextRoundWhenOptionIsFourOrHigher()
                        }
                    }
                }
            }
        }
    }

    //This calculates the user's points this round when the option they choose is 4-12, and takes the user to next round (or result activity)
    private fun calculatePointsAndGoToNextRoundWhenOptionIsFourOrHigher() {
        var roundSum = 0
        for (dice in allDiceArray) {
            if (dice!!.getCombinationNumber() != 0) {
                roundSum += dice.getFace()
            }
        }
        val optionIndex = optionButton.text.toString().toInt() - 3
        allPointsArray[optionIndex] = roundSum
        allOptionButtonsList[optionIndex].isEnabled = false
        resetViewsAndGoToNextRound()
    }

    //This returns true if the user's combinations are all valid for the option they chose.
    private fun areAllCombinationsValid(): Boolean {
        val sumOfCombinationsMutableList = mutableListOf(0, 0, 0, 0, 0, 0)
        for (dice in allDiceArray) {
            when (dice!!.getCombinationNumber()) {
                1 -> sumOfCombinationsMutableList[0] += dice.getFace()
                2 -> sumOfCombinationsMutableList[1] += dice.getFace()
                3 -> sumOfCombinationsMutableList[2] += dice.getFace()
                4 -> sumOfCombinationsMutableList[3] += dice.getFace()
                5 -> sumOfCombinationsMutableList[4] += dice.getFace()
                6 -> sumOfCombinationsMutableList[5] += dice.getFace()
            }
        }
        return showIncorrectValueToast(sumOfCombinationsMutableList)
    }

    //This shows a toast that says that one or more combinations are not valid.
    private fun showIncorrectValueToast(sumOfCombinationsMutableList: MutableList<Int>): Boolean {
        val wantedSum = optionButton.text.toString().toInt()
        for (sum in sumOfCombinationsMutableList) {
            if (sum != 0) {
                if (sum != wantedSum) {
                    Toast.makeText(
                        this,
                        "One or more combinations do not have the correct value",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
        }
        return true
    }

    //This is the alert that is showed when a user wants to go to next round without any points (only option 4-12). If the user presses yes, the next round will start (or they will be directed to the result activity if finished)
    private fun areYouSureAlertToGoToNextRoundWithoutPoints() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("You have not made any combinations, are you sure?")
        builder.setPositiveButton("Yes, go to next round anyways") { _, _ ->
            val optionIndex = optionButton.text.toString().toInt() - 3
            allPointsArray[optionIndex] = 0
            allOptionButtonsList[optionIndex].isEnabled = false
            resetViewsAndGoToNextRound()
        }
        setNegativeButtonAndShowAlert(builder)
    }

    //This calculates the points when the user has chosen the "Low"-option, and takes the user to the next round (or result activity if finished)
    private fun calculatePointsAndGoToNextRoundWhenOptionEqualsLow() {
        var sum = 0
        for (dice in allDiceArray) {
            if (dice!!.getFace() < 4) {
                sum += dice.getFace()
            }
        }
        allOptionButtonsList[0].isEnabled = false
        allPointsArray[0] = sum

        resetViewsAndGoToNextRound()
    }

    //This resets the views that are needed when starting a new round.
    private fun resetViewsAndGoToNextRound() {
        isInCombinationStage = false
        isOptionRadioGroupOpen = false
        throwsLeftInt = 3
        roundInt++

        if (continueButton.text == end) {
            val intent = ResultActivity.newIntent(this@MainActivity, allPointsArray)
            startActivity(intent)
        } else {
            for (dice in allDiceArray) {
                dice!!.resetDiceForNewTurn(allDiceButtonsList[dice.getDiceNo()])
            }
            updateThrowsLeftText()
            updateTotalSumText()
            updateTapTheDiceText()
            continueButton.text = keepAll
            continueButton.text = keepAll
            updateRoundText()
            setInvisibilitiesOnNewGame()
            combinationsLayout.removeAllViews()
            throwButton.visibility = View.VISIBLE
        }
    }

    //This sets the negative button in an Alert to cancel and shows the dialog.
    private fun setNegativeButtonAndShowAlert(builder: AlertDialog.Builder) {
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#689F38"))
    }

    //When the user has chosen an option, the index of that option in the allOptionButtonsList will be stored in a variable currentOptionIndex that is used to know which option the user has pressed on.
    private fun optionsRadioButtonGroupClickHandler() {
        optionsRadioButtonGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            currentOptionIndex = allOptionButtonsList.indexOf(radio)
            setOptionButtonText()
            setVisibilityOfCombinationsNotNeededText()
        }
    }

    //This points the arrow on the optionButton in the correct way.
    //If the arrow is pointing down, pressing it will open up all point system options for the user to choose from.
    //If the arrow is pointing up, pressing it will close all options.
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

    //This changes the visibilities of some view classes depending on how many times the user has pressed the throwButton.
    private fun throwClickHandler() {
        throwButton.setOnClickListener {
            when (throwsLeftInt) {
                3 -> setVisibilitiesAfterFirstThrow()
                1 -> handleViewClassesOnCombine()
            }

            //This randomizes the face of all dice that the user wants to throw
            for (i in allDiceButtonsList.indices) {
                allDiceArray[i]!!.throwDice(allDiceButtonsList[i])
            }

            throwsLeftInt -= 1
            updateThrowsLeftText()
        }
    }

    //This handles the visibility and texts on some views that need to be changed when the user moves on to the combination stage.
    //This also changes the colour of the dice when they are clicked on to red instead of green, to indicate that the user is in the combination stage.
    private fun handleViewClassesOnCombine() {
        setOptionRadioGroupVisibility()
        addCombinationButton.visibility = View.VISIBLE
        continueButton.visibility = View.VISIBLE
        throwButton.visibility = View.GONE
        isInCombinationStage = true
        updateTapTheDiceText()
        setVisibilityOfCombinationsNotNeededText()

        if (roundInt == 10) {
            continueButton.text = end
        } else {
            continueButton.text = nextRound
        }
        for (dice in allDiceArray) {
            dice!!.setThrowable(false)
        }
    }

    //This handles the visibility on some views that need to be changed when the user has made their first throw. This also makes the dice possible to mark if the user wants to keep some when throwing other dice.
    private fun setVisibilitiesAfterFirstThrow() {
        setInvisibilitiesOnNewGame()
        continueButton.visibility = View.VISIBLE
        optionButton.visibility = View.VISIBLE
        tapTheDiceTextView.visibility = View.VISIBLE
        for (button in allDiceButtonsList) {
            button.isEnabled = true
        }
    }

    //This handles what happens when a dice is clicked on.
    private fun diceClickHandler() {
        for (i in allDiceButtonsList.indices) {
            val button = allDiceButtonsList[i]
            val dice = allDiceArray[i]
            button.setOnClickListener {

                //This can make the dice green if the user wants to keep it when throwing other dice.
                //And this can make the dice red if the user is in the stage of making combinations.
                dice!!.clickHandler(button)
            }
        }
    }

    //This handles the visibility on some views that need to be changed when the user starts up a new game.
    private fun setInvisibilitiesOnNewGame() {
        tapTheDiceTextView.visibility = View.GONE
        addCombinationButton.visibility = View.GONE
        combinationsNotNeededTextView.visibility = View.GONE
        optionButton.visibility = View.GONE
        continueButton.visibility = View.GONE
        optionsRadioButtonGroup.visibility = View.GONE
        setOptionRadioGroupVisibility()

        //If the user clicks on a dice, nothing will happen as it should be before a throw has been made.
        for (button in allDiceButtonsList) {
            button.isEnabled = false
        }
    }

    //This sets the correct visibility of the radio group that includes buttons for all of the options a user can make.
    private fun setOptionRadioGroupVisibility() {
        when {
            throwsLeftInt == 3 -> optionsRadioButtonGroup.visibility = View.GONE
            isOptionRadioGroupOpen -> optionsRadioButtonGroup.visibility = View.VISIBLE
            else -> {
                optionsRadioButtonGroup.visibility = View.GONE
            }
        }
    }

    //When the user chooses the "Low"-option, this will show a textView that says that combinations are not needed. If the user chooses another option, the textView will become invisible.
    private fun setVisibilityOfCombinationsNotNeededText() {
        if (isInCombinationStage && currentOptionIndex == 0) {
            combinationsNotNeededTextView.visibility = View.VISIBLE
        } else {
            combinationsNotNeededTextView.visibility = View.GONE
        }
    }


    //This updates the textView that shows which round the user is on
    private fun updateRoundText() {
        val roundText = "$round $roundInt"
        roundTextView.text = roundText
    }

    //When the user has pressed the throwButton, or has continued to the combination stage without using all throws, this will update the user on how many throws are left.
    private fun updateThrowsLeftText() {
        val throwsLeft = "$throwsLeftInt $throwsLeft"
        throwsLeftTextView.text = throwsLeft
    }

    //This updates the text on the textView that tells the user what tapping a dice will do.
    private fun updateTapTheDiceText() {
        if (isInCombinationStage) {
            tapTheDiceTextView.text = tapDiceCombine
            tapTheDiceTextView.setTextColor(Color.RED)
        } else {
            tapTheDiceTextView.text = tapDiceKeep
            tapTheDiceTextView.setTextColor(Color.parseColor("#689F38"))
        }
    }

    //This changes the text on the optionButton depending on which option the user has chosen.
    private fun setOptionButtonText() {
        if (currentOptionIndex == -1) {
            optionButton.text = option
        } else {
            val text = allOptionButtonsList[currentOptionIndex].text
            optionButton.text = text
        }
    }

    //This updates the textView that shows the total sum the user has gathered throughout the game so far
    private fun updateTotalSumText() {
        var sum = 0
        for (int in allPointsArray) {
            sum += int
        }
        totalSumInt = sum
        val text = "$totalSumText $totalSumInt"
        totalSumTextView.text = text
    }

    //This adds the 6 Dice objects to the allDiceArray.
    private fun setArrayOfDice() {
        allDiceArray[0] = diceOne
        allDiceArray[1] = diceTwo
        allDiceArray[2] = diceThree
        allDiceArray[3] = diceFour
        allDiceArray[4] = diceFive
        allDiceArray[5] = diceSix
    }

    //This accesses all views from activity_main.xml in the activity
    private fun findAllViewsByID() {
        roundTextView = findViewById(R.id.round_text_view)
        throwsLeftTextView = findViewById(R.id.throws_left_text_view)
        allDiceButtonsList = listOf(
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
        allOptionButtonsList = listOf(
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

    //This returns a list which objects are true if the user has already used an option.
    private fun getWhichOptionsThatHaveBeenUsed(): BooleanArray {
        for (option in allOptionButtonsList) {
            if (!option.isEnabled) {
                areAllOptionsUsed[allOptionButtonsList.indexOf(option)] = true
            }
        }
        return areAllOptionsUsed
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