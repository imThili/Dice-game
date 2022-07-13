package se.umu.thek0034.thirty2point0

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

//This class stores all information that a dice has: face, isMarked, isThrowable, isRed, isVisible
class Dice (private val diceNo : Int) : Parcelable {

    private var face: Int = 0
    private var marked: Boolean = false
    private var throwable: Boolean = true
    private var red: Boolean = false
    private var visible: Boolean = true
    private var combinationNumber: Int = 0

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        face = parcel.readInt()
        marked = parcel.readByte() != 0.toByte()
        throwable = parcel.readByte() != 0.toByte()
        red = parcel.readByte() != 0.toByte()
        visible = parcel.readByte() != 0.toByte()
        combinationNumber = parcel.readInt()
    }

    fun isRed():Boolean{
        return red
    }

    fun isVisible():Boolean{
        return visible
    }

    fun setVisible(visible : Boolean){
        this.visible = visible
    }

    fun getFace(): Int {
        return face
    }

    fun isMarked(): Boolean {
        return this.marked
    }

    private fun setFace(face: Int) {
        this.face = face
    }

    fun getDiceNo() : Int{
        return this.diceNo
    }

    fun setThrowable(throwable: Boolean) {
        this.throwable = throwable
    }

    fun clickHandler(imageButton: ImageButton){
        if (face != 0 && throwable) {
            if (this.marked) {
                marked = false
                setWhite(imageButton)
            } else {
                marked = true
                setGreen(imageButton)
            }
        } else if (face != 0 && !throwable) {
            if (this.marked) {
                marked = false
                setWhite(imageButton)
            } else {
                marked = true
                setRed(imageButton)
            }
        }
    }

    fun setMarked(marked: Boolean, imageButton : ImageButton) {
        this.marked = marked
        if (marked && throwable)
            setGreen(imageButton)
        else if (marked && !throwable)
            setRed(imageButton)
        else
            setWhite(imageButton)
    }

    fun throwDice(imageButton : ImageButton) {
        imageButton.isEnabled = true
        if (!this.marked) {
            face = (1..6).random()
            setWhite(imageButton)
        }
    }

    fun resetDiceForNewTurn(imageButton : ImageButton){
        this.marked = false
        this.throwable = true
        setFace(diceNo + 1)
        imageButton.isEnabled = false
        imageButton.visibility = View.VISIBLE
        setWhite(imageButton)
    }

    fun setImageOfDiceForCombination(imageView: ImageView){
        when (face) {
            1 -> imageView.setImageResource(R.drawable.small1)
            2 -> imageView.setImageResource(R.drawable.small2)
            3 -> imageView.setImageResource(R.drawable.small3)
            4 -> imageView.setImageResource(R.drawable.small4)
            5 -> imageView.setImageResource(R.drawable.small5)
            6 -> imageView.setImageResource(R.drawable.small6)
        }
    }

    private fun setWhite(imageButton: ImageButton) {
        when (face) {
            1 -> imageButton.setImageResource(R.drawable.white1)
            2 -> imageButton.setImageResource(R.drawable.white2)
            3 -> imageButton.setImageResource(R.drawable.white3)
            4 -> imageButton.setImageResource(R.drawable.white4)
            5 -> imageButton.setImageResource(R.drawable.white5)
            6 -> imageButton.setImageResource(R.drawable.white6)
        }
        red = false
    }

    private fun setGreen(imageButton: ImageButton){
        when (face) {
            1 -> imageButton.setImageResource(R.drawable.green1)
            2 -> imageButton.setImageResource(R.drawable.green2)
            3 -> imageButton.setImageResource(R.drawable.green3)
            4 -> imageButton.setImageResource(R.drawable.green4)
            5 -> imageButton.setImageResource(R.drawable.green5)
            6 -> imageButton.setImageResource(R.drawable.green6)
        }
        red = false
    }

    private fun setRed(imageButton: ImageButton) {
        when (face) {
            1 -> imageButton.setImageResource(R.drawable.red1)
            2 -> imageButton.setImageResource(R.drawable.red2)
            3 -> imageButton.setImageResource(R.drawable.red3)
            4 -> imageButton.setImageResource(R.drawable.red4)
            5 -> imageButton.setImageResource(R.drawable.red5)
            6 -> imageButton.setImageResource(R.drawable.red6)
        }
        red = true
    }

    override fun toString(): String {
        return "Dice: $diceNo, Face: $face"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(diceNo)
        parcel.writeInt(face)
        parcel.writeByte(if (marked) 1 else 0)
        parcel.writeByte(if (throwable) 1 else 0)
        parcel.writeByte(if (red) 1 else 0)
        parcel.writeByte(if (visible) 1 else 0)
        parcel.writeInt(combinationNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dice> {
        override fun createFromParcel(parcel: Parcel): Dice {
            return Dice(parcel)
        }

        override fun newArray(size: Int): Array<Dice?> {
            return arrayOfNulls(size)
        }
    }

}