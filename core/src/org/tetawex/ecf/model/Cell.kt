package org.tetawex.ecf.model

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedSet
import org.tetawex.ecf.util.IntVector2

/**
 * Created by Tetawex on 02.05.17.
 */
//TODO: Extract interface!!!
class Cell {
    var position: IntVector2? = null
    var elements: OrderedSet<Element>? = null

    constructor(position: IntVector2, vararg elements: Element) {
        this.elements = OrderedSet()
        this.elements!!.addAll(*elements)
        this.position = position
    }

    @JvmOverloads constructor(position: IntVector2, elements: OrderedSet<Element> = OrderedSet()) {
        this.position = position
        this.elements = elements
    }

    fun setElements(vararg elementArray: Element) {
        elements!!.addAll(*elementArray)
    }

    fun clear() {
        elements!!.clear()
    }

    fun interactWith(cell: Cell): Int {
        val array = Array<Element>()

        array.addAll(elements!!.orderedItems())
        array.addAll(cell.elements!!.orderedItems())
        cell.clear()

        return ElementFunctions.advancedResolveElementSet(array, elements)
    }
}
