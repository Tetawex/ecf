package org.tetawex.ecf.model

import org.tetawex.ecf.util.IntVector2

/**
 * Created by Tetawex on 23.05.2017.
 */
object CellArrayFactory {
    fun generateBasicCellArray(width: Int, height: Int): Array<Array<Cell?>> {
        val cellArray = Array(width) { Array<Cell?>(height) { null } }
        for (i in 0 until width) {
            for (j in 0 until height) {
                cellArray[i][j] = CellFactory.generateRandomNonEmptyCell(IntVector2(i, j))
            }
        }
        return cellArray
    }

    fun generateEmptyCellArray(width: Int, height: Int): Array<Array<Cell?>> {
        val cellArray = Array<Array<Cell?>>(width) { arrayOfNulls(height) }
        for (i in 0 until width) {
            for (j in 0 until height) {
                cellArray[i][j] = CellFactory.generateEmptyCell(IntVector2(i, j))
            }
        }
        return cellArray
    }

    fun generateTutorialCellArray(): Array<Array<Cell?>> {
        val cellArray = Array<Array<Cell?>>(4) { arrayOfNulls(4) }
        for (i in 0..3) {
            for (j in 0..3) {
                cellArray[i][j] = CellFactory.generateEmptyCell(IntVector2(i, j))
            }
        }
        cellArray[0][0] = null
        cellArray[2][0] = null
        cellArray[0][1] = null
        cellArray[1][0] = null
        cellArray[0][3] = null
        cellArray[1][3] = null

        cellArray[0][2]!!.setElements(Element.WATER, Element.AIR, Element.LIGHT)
        cellArray[1][1]!!.setElements(Element.FIRE, Element.EARTH)
        cellArray[1][2]!!.setElements(Element.FIRE, Element.SHADOW)
        cellArray[2][1]!!.setElements(Element.FIRE)
        cellArray[2][2]!!.setElements(Element.AIR)
        cellArray[2][3]!!.setElements(Element.AIR)
        cellArray[3][0]!!.setElements(Element.WATER)
        cellArray[3][1]!!.setElements(Element.EARTH)
        cellArray[3][2]!!.setElements(Element.LIGHT)
        cellArray[3][3]!!.setElements(Element.SHADOW)

        return cellArray
    }
}
