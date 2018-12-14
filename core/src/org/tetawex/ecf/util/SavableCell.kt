package org.tetawex.ecf.util

import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.model.Element

import java.util.Arrays

/**
 * Created by tetawex on 16.08.17.
 */
class SavableCell(cell: Cell?) {
    private var position: IntVector2? = null
    private var elements: List<Element>? = null
    private var noCell: Boolean = false

    init {
        if (cell != null) {
            elements = Arrays.asList(*cell.elements!!.orderedItems().items)
            position = cell.position
        } else
            noCell = true
    }

    fun createCell(): Cell? {
        if (noCell) {
            return null
        }
        val cell = Cell(position!!)
        for (e in elements!!) {
            if (e != null)
                cell.elements!!.add(e)
        }
        return cell
    }
}
