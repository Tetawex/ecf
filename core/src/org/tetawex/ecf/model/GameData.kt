package org.tetawex.ecf.model

import com.badlogic.gdx.utils.OrderedSet
import org.tetawex.ecf.util.IntVector2

/**
 * Created by Tetawex on 19.05.2017.
 */
class GameData {

    var gameDataChangedListener: GameDataChangedListener? = null

    var fire: Int = 0
    var water: Int = 0
    var air: Int = 0
    var earth: Int = 0
    var shadow: Int = 0
    var light: Int = 0
    private var time: Int = 0

    var maxScore: Int = 0

    private var mana: Int = 0
    private var score: Int = 0
    var cellArray: Array<Array<Cell?>> = arrayOf(arrayOf())
        set(inputArray) {
            val width = inputArray.size
            var height = 0
            if (width > 0)
                height = inputArray[0].size

            val copiedArray = Array<Array<Cell?>>(width) { arrayOfNulls(height) }

            for (i in 0 until width) {
                for (j in 0 until height) {
                    if (inputArray[i][j] != null)
                        copiedArray[i][j] =
                            Cell(IntVector2(i, j), copyElementSet(inputArray[i][j]!!.elements))
                }
            }
            field = copiedArray
            gameDataChangedListener!!.cellMapChanged(this.cellArray)

            calculateElementsLeft()
        }

    interface GameDataChangedListener {
        fun manaChanged(newValue: Int)

        fun scoreChanged(newValue: Int)

        fun cellMapChanged(newMap: Array<Array<Cell?>>)

        fun elementsCountChanged(
            fire: Int,
            water: Int,
            air: Int,
            earth: Int,
            shadow: Int,
            light: Int,
            time: Int
        )

        fun gameLostOrWon(payload: WinLossPayload)
    }

    data class WinLossTotals(
        val rawScore: Int,
        val manaScore: Int,
        val totalScore: Int,
        val stars: Int,
        val maxScore: Int
    )

    sealed class WinLossPayload(val totals: WinLossTotals) {
        class Win(totals: WinLossTotals) : WinLossPayload(
            totals
        )

        class Loss(
            val lossCondition: LossCondition
        ) : WinLossPayload(
            totals = WinLossTotals(0, 0, 0, 0, 0)
        )
    }

    enum class LossCondition {
        NO_MANA, NO_FIRE, NO_WATER, NO_AIR, NO_EARTH, NO_SHADOW, NO_LIGHT, NO_TIME
    }

    fun getMana(): Float {
        return mana.toFloat()
    }

    fun setMana(mana: Int) {
        var mana = mana
        this.mana = mana
        if (mana < 0)
            mana = 0
        gameDataChangedListener!!.manaChanged(mana)
    }

    fun getScore(): Int {
        return score
    }

    fun setScore(score: Int) {
        var score = score
        this.score = score
        if (score < 0)
            score = 0
        gameDataChangedListener!!.scoreChanged(score)
    }

    private fun copyElementSet(oldSet: OrderedSet<Element>?): OrderedSet<Element> {
        val set = OrderedSet<Element>()
        set.addAll(oldSet!!)
        return set
    }

    fun spendManaOnMove(movedElementsCount: Int) {
        setMana(mana - SINGLE_MOVE_MANACOST)
    }

    fun processElementsMerge(mergedElementsCount: Int) {
        setMana(mana + SINGLE_MERGE_BONUS_MANA * mergedElementsCount)
        setScore(score + getTotalMergeScore(mergedElementsCount))
        calculateElementsLeft()
        checkIfLostOrWon()
    }

    private fun getTotalMergeScore(mergedElementsCount: Int): Int {
        if (mergedElementsCount > 2)
            return 20 * SINGLE_MERGE_BONUS_SCORE
        if (mergedElementsCount > 1)
            return 5 * SINGLE_MERGE_BONUS_SCORE
        return if (mergedElementsCount > 0) SINGLE_MERGE_BONUS_SCORE else 0
    }

    private fun getTotalMergeMana(mergedElementsCount: Int): Int {
        return SINGLE_MERGE_BONUS_MANA * mergedElementsCount
    }

    fun canMove(cellElementCount: Int): Boolean {
        return mana >= SINGLE_MOVE_MANACOST
    }

    private fun calculateElementsLeft() {
        fire = 0
        water = 0
        earth = 0
        air = 0
        shadow = 0
        light = 0
        time = 0
        for (i in this.cellArray.indices) {
            for (j in 0 until this.cellArray[i].size) {
                if (this.cellArray[i][j] != null) {
                    if (this.cellArray[i][j]!!.elements.contains(Element.FIRE))
                        fire++
                    if (this.cellArray[i][j]!!.elements.contains(Element.WATER))
                        water++
                    if (this.cellArray[i][j]!!.elements.contains(Element.EARTH))
                        earth++
                    if (this.cellArray[i][j]!!.elements.contains(Element.AIR))
                        air++
                    if (this.cellArray[i][j]!!.elements.contains(Element.SHADOW))
                        shadow++
                    if (this.cellArray[i][j]!!.elements.contains(Element.LIGHT))
                        light++
                    if (this.cellArray[i][j]!!.elements.contains(Element.TIME))
                        time++
                }

            }
        }
        triggerElementsCountChangedEvent()
    }

    private fun checkIfLostOrWon() {
        val rawScore = score
        val manaScore = mana * 100
        val totalScore = rawScore + manaScore

        val stars = when {
            (totalScore >= maxScore || totalScore == 0) -> 3
            totalScore > maxScore / 2 -> 2
            else -> 1
        }

        val totals = WinLossTotals(
            rawScore,
            manaScore,
            totalScore,
            stars,
            maxScore
        )

        if (fire == 0 && water == 0 && earth == 0 && air == 0 && shadow == 0 && light == 0 && time == 0) {
            gameDataChangedListener?.gameLostOrWon(
                WinLossPayload.Win(totals)
            )
        } else {
            val lossCondition = when {
                mana <= 0 -> LossCondition.NO_MANA
                fire == 0 && water != 0 -> LossCondition.NO_FIRE
                fire != 0 && water == 0 -> LossCondition.NO_WATER
                air == 0 && earth != 0 -> LossCondition.NO_AIR
                earth == 0 && air != 0 -> LossCondition.NO_EARTH
                shadow == 0 && light != 0 -> LossCondition.NO_SHADOW
                light == 0 && shadow != 0 -> LossCondition.NO_LIGHT
                time % 2 != 0 -> LossCondition.NO_TIME
                else -> null
            }

            lossCondition?.let {
                gameDataChangedListener?.gameLostOrWon(
                    WinLossPayload.Loss(it)
                )
            }
        }

    }

    private fun triggerElementsCountChangedEvent() {
        gameDataChangedListener!!.elementsCountChanged(fire, water, air, earth, shadow, light, time)
    }

    companion object {
        private val SINGLE_MERGE_BONUS_SCORE = 100
        private val SINGLE_MERGE_BONUS_MANA = 2
        private val SINGLE_MOVE_MANACOST = 1
    }
}
