package org.tetawex.ecf.model

import org.tetawex.ecf.util.IntVector2

/**
 * Created by Tetawex on 17.05.2017.
 */
object CellFactory {
    fun generateRandomNonEmptyCell(position: IntVector2): Cell {
        return Cell(position, ElementFunctions.generateRandomNonEmptyElementSet())
    }

    fun generateRandomCell(position: IntVector2): Cell {
        return Cell(position, ElementFunctions.generateRandomElementSet())
    }

    fun generateEmptyCell(position: IntVector2): Cell {
        return Cell(position)
    }
}
