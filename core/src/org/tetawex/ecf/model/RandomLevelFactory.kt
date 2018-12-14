package org.tetawex.ecf.model

import com.badlogic.gdx.math.RandomXS128
import org.tetawex.ecf.util.IntVector2
import org.tetawex.ecf.util.RandomProvider

import java.util.*

/**
 * Created by 1 on 06.06.2017.
 */
class RandomLevelFactory(seed: Int, private val width: Int, private val height: Int) {
    val theBoard: Array<Array<Cell?>>
    private var cellSetAll: MutableSet<Cell?>? = null
    private var cellSetNoElements: MutableSet<Cell?>? = null
    private var cellSetWithElements: MutableSet<Cell?>? = null
    private val random: RandomXS128
    var mana = 2
        private set

    init {
        RandomProvider.setSeed(seed.toLong())
        random = RandomProvider.getRandom()
        theBoard = CellArrayFactory.generateEmptyCellArray(width, height)
        createNullCells()
        createCellSets()
        fillTheBoard(width + height)
    }

    private fun createNullCells() {

        val iterations = Math.min(width, height) + 1
        var x: Int
        var y: Int
        for (i in 0 until iterations) {
            if (random.nextBoolean()) {
                x = random.nextInt(width)
                y = random.nextInt(height)
                theBoard[x][y] = null
            }
        }
    }

    private fun createCellSets() {
        cellSetAll = HashSet()
        cellSetWithElements = HashSet()
        cellSetNoElements = HashSet()
        for (x in 0 until width) {
            for (y in 0 until height) {
                val cell = getCell(IntVector2(x, y))
                if (cell != null) {
                    cellSetAll!!.add(cell)
                    cellSetNoElements!!.add(cell)
                }
            }
        }
    }

    private fun fillTheBoard(iterations: Int) {
        preFill()
        for (i in 0 until iterations) {
            val rnd = random.nextInt(100)
            if (rnd < 20) {
                moveCell()
            } else if (rnd < 30) {
                splitCell()
            } else if (rnd < 50) {
                createOppositeElements()
            } else if (rnd < 100) {
                createIdenticalElements()
            }
        }
    }

    private fun preFill() {
        var cell1: Cell?
        var cell2: Cell?
        cell1 = getRandomCellFromSet(cellSetAll!!)
        cell2 = getRandomNeighbour(cell1)
        cellSetNoElements!!.remove(cell1)
        cellSetWithElements!!.add(cell1)
        cellSetNoElements!!.remove(cell2)
        cellSetWithElements!!.add(cell2)
        cell1!!.elements!!.add(Element.FIRE)
        cell2!!.elements!!.add(Element.WATER)

        cell1 = getRandomCellFromSet(cellSetAll!!)
        cell2 = getRandomNeighbour(cell1)
        cellSetNoElements!!.remove(cell1)
        cellSetWithElements!!.add(cell1)
        cellSetNoElements!!.remove(cell2)
        cellSetWithElements!!.add(cell2)
        cell1!!.elements!!.add(Element.LIGHT)
        cell2!!.elements!!.add(Element.SHADOW)

        cell1 = getRandomCellFromSet(cellSetAll!!)
        cell2 = getRandomNeighbour(cell1)
        cellSetNoElements!!.remove(cell1)
        cellSetWithElements!!.add(cell1)
        cellSetNoElements!!.remove(cell2)
        cellSetWithElements!!.add(cell2)
        cell1!!.elements!!.add(Element.EARTH)
        cell2!!.elements!!.add(Element.AIR)
    }

    private fun moveCell() {
        val from = getRandomCellFromSet(cellSetWithElements!!) ?: return
        val neighbours = getNeighoubrsEmpty(from)
        if (neighbours.size != 0) {
            ++mana
            val to = getRandom(neighbours)
            cellSetNoElements!!.remove(to)
            cellSetWithElements!!.add(to)
            cellSetWithElements!!.remove(from)
            cellSetNoElements!!.add(from)

            for (i in 0 until from.elements!!.size) {
                val element = from.elements!!.first()
                to.elements!!.add(element)
                from.elements!!.remove(element)
            }
        }
    }

    private fun splitCell() {
        var from: Cell? = null
        for (i in 0..4) {
            from = getRandomCellFromSet(cellSetWithElements!!)
            if (from != null && from.elements!!.size > 1) break
        }
        if (from == null || from.elements!!.size == 1) return

        val neighbours = getNeighoubrsEmpty(from)
        if (neighbours.size != 0) {
            ++mana
            val to = getRandom(neighbours)
            cellSetNoElements!!.remove(to)
            cellSetWithElements!!.add(to)
            val amount = random.nextInt(from.elements!!.size - 1) + 1
            if (amount == from.elements!!.size) {
                cellSetWithElements!!.remove(from)
                cellSetNoElements!!.add(from)
            }
            for (i in 0 until amount) {
                val element = from.elements!!.first()
                to.elements!!.add(element)
                from.elements!!.remove(element)
            }
        }
    }

    private fun createOppositeElements() {
        val from = getRandomCellFromSet(cellSetNoElements!!) ?: return
        val neighbours = getNeighoubrsEmpty(from)
        if (neighbours.size != 0) {

            val to = getRandom(neighbours)
            cellSetNoElements!!.remove(from)
            cellSetWithElements!!.add(from)
            cellSetNoElements!!.remove(to)
            cellSetWithElements!!.add(to)

            from.elements = ElementFunctions.generateRandomElementSet()
            val size = from.elements!!.size
            if (mana > 1 + size) {
                mana -= size
            } else {
                mana = 1
            }
            for (element in from.elements!!) {
                to.elements!!.add(Element.getOpposite(element))
            }
        }
    }

    private fun createIdenticalElements() {
        val from = getRandomCellFromSet(cellSetWithElements!!) ?: return
        val neighbours = getNeighoubrsEmpty(from)
        if (neighbours.size != 0) {
            ++mana
            val to = getRandom(neighbours)
            cellSetNoElements!!.remove(to)
            cellSetWithElements!!.add(to)
            for (element in from.elements!!) {
                to.elements!!.add(element)
            }
        }
    }

    private fun getCell(pos: IntVector2): Cell? {
        if (!(0 <= pos.x && pos.x < width)) {
            return null
        }
        return if (!(0 <= pos.y && pos.y < height)) {
            null
        } else theBoard[pos.x][pos.y]
    }

    fun getNeighbours(cell: Cell?): List<Cell?> {
        val res = ArrayList<Cell?>()
        val pos = cell!!.position
        var neighbour: Cell?
        val x = pos!!.x
        val y = pos.y
        if (x % 2 != 0) {
            neighbour = getCell(IntVector2(x - 1, y + 1))
            if (neighbour != null) res.add(neighbour)

            neighbour = getCell(IntVector2(x + 1, y + 1))
            if (neighbour != null) res.add(neighbour)
        } else {
            neighbour = getCell(IntVector2(x - 1, y - 1))
            if (neighbour != null) res.add(neighbour)

            neighbour = getCell(IntVector2(x + 1, y - 1))
            if (neighbour != null) res.add(neighbour)
        }
        neighbour = getCell(IntVector2(x - 1, y))
        if (neighbour != null) res.add(neighbour)

        neighbour = getCell(IntVector2(x + 1, y))
        if (neighbour != null) res.add(neighbour)

        neighbour = getCell(IntVector2(x, y + 1))
        if (neighbour != null) res.add(neighbour)

        neighbour = getCell(IntVector2(x, y - 1))
        if (neighbour != null) res.add(neighbour)

        return res
    }

    private fun getNeighoubrsEmpty(cell: Cell): List<Cell?> {
        val neighbours = getNeighbours(cell)
        val res = ArrayList<Cell?>()
        for (neighbour in neighbours) {
            if (neighbour!!.elements!!.size == 0) {
                res.add(neighbour)
            }
        }

        return res
    }

    private fun getNeighboursNotEmpty(cell: Cell): List<Cell?> {
        val neighbours = getNeighbours(cell)
        val res = ArrayList<Cell?>()
        for (neighbour in neighbours) {
            if (neighbour!!.elements!!.size != 0) {
                res.add(neighbour)
            }
        }

        return res
    }

    private fun getRandom(cells: List<Cell?>): Cell {
        return cells[random.nextInt(cells.size)]!!
    }

    private fun getRandomNeighbour(cell: Cell?): Cell? {
        val neighbours = getNeighbours(cell)
        return neighbours[random.nextInt(neighbours.size)]
    }

    private fun getRandomCellFromSet(set: Set<Cell?>): Cell? {
        if (set.size == 0) return null
        val index = random.nextInt(set.size)
        val iter = set.iterator()
        for (i in 0 until index) {
            iter.next()
        }
        return iter.next()
    }
}
