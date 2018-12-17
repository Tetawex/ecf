package org.tetawex.ecf.model

import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedSet
import org.tetawex.ecf.util.IntVector2

/**
 * Created by Tetawex on 02.05.17.
 */
//TODO: Extract interface!!!
class Cell @JvmOverloads constructor(var position: IntVector2,
                                     var elements: OrderedSet<Element> = OrderedSet()) {

    fun setElements(vararg elementArray: Element) {
        elements.addAll(*elementArray)
    }

    fun clear() {
        elements.clear()
    }

    fun interactWith(cell: Cell): Int {
        val array = Array<Element>()

        array.addAll(elements.orderedItems())
        array.addAll(cell.elements.orderedItems())
        cell.clear()

        return ElementFunctions.advancedResolveElementSet(array, elements)
    }
}
