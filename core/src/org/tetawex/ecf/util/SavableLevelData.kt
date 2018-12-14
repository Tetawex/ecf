package org.tetawex.ecf.util

import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.model.LevelData

/**
 * Created by tetawex on 16.08.17.
 */
class SavableLevelData(data: LevelData) {
    private val cellArray: Array<Array<SavableCell?>>
    private val mana: Int
    private val maxScore: Int
    private val name: String?

    init {
        mana = data.mana
        maxScore = data.maxScore
        name = data.name

        val width = data.cellArray.size
        val height = data.cellArray[0].size
        cellArray = Array(width) { arrayOfNulls<SavableCell>(height) }

        for (i in 0 until width) {
            for (j in 0 until height) {
                cellArray[i][j] = SavableCell(data.cellArray[i][j])
            }
        }
    }

    fun createLevelData(): LevelData {
        val levelData = LevelData()
        levelData.levelNumber = 0
        levelData.maxScore = maxScore
        levelData.levelCode = "editor"
        levelData.mana = mana
        levelData.name = name

        val width = cellArray.size
        val height = cellArray[0].size
        val trueCellArray = Array<Array<Cell?>>(width) { arrayOfNulls(height) }

        for (i in 0 until width) {
            for (j in 0 until height) {
                trueCellArray[i][j] = cellArray[i][j]!!.createCell()
            }
        }

        levelData.cellArray = trueCellArray
        return levelData
    }
}
