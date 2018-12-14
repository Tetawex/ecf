package org.tetawex.ecf.model

import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.OrderedSet
import org.tetawex.ecf.util.RandomProvider

/**
 * Created by Tetawex on 17.05.2017.
 */
object ElementFunctions {
    fun generateRandomElementSet(): OrderedSet<Element> {
        val random = RandomProvider.getRandom()
        val set = OrderedSet<Element>()
        for (i in 0 until Element.elementsCount) {
            if (random.nextBoolean()) {
                set.add(Element.getElementById(i + 1))
            }
        }
        resolveElementSet(set)
        return set
    }

    fun generateRandomNonEmptyElementSet(): OrderedSet<Element> {
        val random = RandomProvider.getRandom()
        val set = OrderedSet<Element>()
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.FIRE)
            else
                set.add(Element.WATER)
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.EARTH)
            else
                set.add(Element.AIR)
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.LIGHT)
            else
                set.add(Element.SHADOW)
        }
        if (set.size == 0) {
            set.add(Element.getElementById(1 + random.nextInt(Element.elementsCount - 1)))
        }
        return set
    }

    fun resolveElementSet(elements: OrderedSet<Element>): Int {

        val deletionList = Array<Element>()
        var reactions = 0
        for (element in elements) {
            val oppositeElement = Element.getOpposite(element)
            if (elements.contains(oppositeElement) && !deletionList.contains(element, true)) {
                deletionList.add(element)
                deletionList.add(oppositeElement)
                reactions++
            }
        }
        for (e in deletionList) {
            elements.remove(e)
        }
        return reactions
    }

    fun advancedResolveElementSet(elements: Array<Element>, targetSet: OrderedSet<Element>?): Int {

        val deletionList = Array<Element>()
        for (e in elements) {
            if (e == Element.TIME)
                deletionList.add(e)
        }
        elements.removeAll(deletionList, true)
        targetSet!!.clear()
        targetSet.addAll(elements)
        if (deletionList.size % 2 == 0) {
            return resolveElementSet(targetSet)
        } else {
            targetSet.add(Element.TIME)
            return 0
        }
    }

    fun addElementToCell(cell: Cell, element: Element) {
        val result = Array<Element>()
        result.addAll(cell.elements!!.orderedItems())
        result.add(element)
        advancedResolveElementSet(result, cell.elements)
    }
}
