package org.tetawex.ecf.presentation.actor

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.utils.OrderedMap
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.model.CellFactory
import org.tetawex.ecf.model.Element
import org.tetawex.ecf.model.ElementFunctions
import org.tetawex.ecf.util.IntVector2
import org.tetawex.ecf.util.MathUtils

/**
 * Created by tetawex on 02.05.17.
 */
class EditorHexMapActor(game: ECFGame) : BaseWidget<ECFGame>(game) {

    private var _cellArray: Array<Array<Cell?>>

    var cellActionListener: CellActionListener? = null

    var soundVolume = 1f
    /*@Override
    public void setWidth(float width){
        super.setWidth(width);
        hexagonWidth=width/cellArray.length;
    }
    @Override
    public void setHeight(float height){
        super.setWidth(height);
        hexagonHeight=height/cellArray[0].length;
    }*/
    var scalingFactor = 1f

    private var hexagonWidth: Float = 0.toFloat()
    private var hexagonHeight: Float = 0.toFloat()

    private var elementWidth = 130f
    private var elementHeight = 130f

    var selectedCell: Cell? = null
        private set
    private var selectedPosition = IntVector2(0, 0)

    private val cellRegion: TextureRegion
    private val nullRegion: TextureRegion
    private val selectedRegion: TextureRegion

    private val clickSound: Sound
    private val errorSound: Sound

    private val textureToElementMap: OrderedMap<Element, TextureRegion>

    interface CellActionListener {
        fun cellMerged(mergedElementsCount: Int)

        fun cellMoved(cellElementCount: Int)

        fun canMove(cellElementCount: Int): Boolean
    }

    init {

        cellRegion = game.getTextureRegionFromAtlas("hexagon")
        selectedRegion = game.getTextureRegionFromAtlas("hexagon_selected")
        nullRegion = game.getTextureRegionFromAtlas("hexagon_disabled")

        clickSound = game.assetManager.get("sounds/click.ogg", Sound::class.java)
        errorSound = game.assetManager.get("sounds/error.ogg", Sound::class.java)

        textureToElementMap = OrderedMap()
        textureToElementMap.put(Element.FIRE, game
                .getTextureRegionFromAtlas("element_fire"))
        textureToElementMap.put(Element.WATER, game
                .getTextureRegionFromAtlas("element_water"))
        textureToElementMap.put(Element.AIR, game
                .getTextureRegionFromAtlas("element_air"))
        textureToElementMap.put(Element.EARTH, game
                .getTextureRegionFromAtlas("element_earth"))
        textureToElementMap.put(Element.SHADOW, game
                .getTextureRegionFromAtlas("element_shadow"))
        textureToElementMap.put(Element.LIGHT, game
                .getTextureRegionFromAtlas("element_light"))
        textureToElementMap.put(Element.TIME, game
                .getTextureRegionFromAtlas("element_time"))

        hexagonHeight = 350f
        hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio

        _cellArray = arrayOf(arrayOf())

        width = hexagonWidth
        height = hexagonHeight

        touchable = Touchable.enabled
        addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                 pointer: Int, button: Int) {
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {}

            override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                   pointer: Int, button: Int): Boolean {
                processIndexClick(getCellIndexByVector(Vector2(x, y)))
                return true
            }
        })
    }

    override fun act(deltaTime: Float) {}

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val nullVector = IntVector2(0, 0)
        //draw hexagons
        for (i in _cellArray.indices) {
            for (j in 0 until _cellArray[i].size) {
                if (cellExistsAt(i, j)) {
                    val vec = _cellArray[i][j]!!.position
                    val offset = findOffsetForIndex(vec!!)
                    batch!!.draw(cellRegion, x + vec.x * (hexagonWidth + offset.x),
                            y + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight)
                } else {
                    nullVector.x = i
                    nullVector.y = j
                    val offset = findOffsetForIndex(nullVector)
                    batch!!.draw(nullRegion, x + nullVector.x * (hexagonWidth + offset.x),
                            y + nullVector.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight)
                }
            }
        }
        //draw selected cells
        run {
            val offset = findOffsetForIndex(selectedPosition)
            batch!!.draw(selectedRegion, x + selectedPosition.x * (hexagonWidth + offset.x),
                    y + selectedPosition.y * hexagonHeight + offset.y,
                    hexagonWidth, hexagonHeight)
        }

        //draw elements
        for (i in _cellArray.indices) {
            for (j in 0 until _cellArray[i].size) {
                if (cellExistsAt(i, j)) {
                    val vec = _cellArray[i][j]!!.position
                    val offset = findOffsetForIndex(vec!!)
                    val cell = _cellArray[i][j]
                    val elements = cell!!.elements!!.orderedItems()
                    val size = cell!!.elements!!.size
                    if (size == 0)
                        continue
                    else if (size == 1)
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    else if (size == 2) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else if (size == 3) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.66f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.33f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(2)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.33f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else if (size == 4) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.66f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.33f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(2)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                (y + j * hexagonHeight + offset.y + hexagonHeight * 0.33f
                                        + hexagonHeight * 0.050f) - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(3)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else if (size == 5) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(2)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(3)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(4)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else if (size == 6) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(2)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(3)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(4)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(5)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else if (size == 7) {
                        batch!!.draw(textureToElementMap.get(elements.get(0)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(1)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(2)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(3)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(4)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.33f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(5)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth * 0.66f) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                        batch.draw(textureToElementMap.get(elements.get(6)), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight)
                    } else
                        batch!!.draw(textureToElementMap.get(Element.EARTH), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - 3 / 2,
                                3f, 3f)
                }
            }
        }
    }

    private fun getCellIndexByVector(vector: Vector2): IntVector2 {
        val i = ((vector.x - hexagonWidth * 0.125f) / (hexagonWidth * 0.75f)).toInt()
        val j = if (i % 2 == 0)
            (vector.y / hexagonHeight).toInt()
        else
            ((vector.y + hexagonHeight * 0.5f) / hexagonHeight - 1).toInt()
        return IntVector2(i, j)
    }

    private fun getCellByIndex(vector: IntVector2): Cell? {
        return _cellArray[vector.x][vector.y]
    }

    fun isAdjacent(pos1: IntVector2, pos2: IntVector2): Boolean {
        if (pos1.x % 2 != 0) {
            if (pos1.x - 1 == pos2.x && pos1.y + 1 == pos2.y)
                return true
            if (pos1.x + 1 == pos2.x && pos1.y + 1 == pos2.y)
                return true
        } else {
            if (pos1.x - 1 == pos2.x && pos1.y - 1 == pos2.y)
                return true
            if (pos1.x + 1 == pos2.x && pos1.y - 1 == pos2.y)
                return true
        }
        if (pos1.x - 1 == pos2.x && pos1.y == pos2.y)
            return true
        if (pos1.x + 1 == pos2.x && pos1.y == pos2.y)
            return true
        if (pos1.x == pos2.x && pos1.y + 1 == pos2.y)
            return true
        return if (pos1.x == pos2.x && pos1.y - 1 == pos2.y) true else false
    }

    private fun processIndexClick(position: IntVector2) {
        if (cellExistsAt(position))
            selectedCell = getCellByIndex(position)
        else
            selectedCell = null
        selectedPosition = position
        clickSound.play(soundVolume)

        if (selectedPosition.x >= _cellArray.size)
            selectedPosition.x = _cellArray.size - 1
        if (selectedPosition.y >= _cellArray[0].size)
            selectedPosition.y = _cellArray[0].size - 1
        if (selectedPosition.x < 0)
            selectedPosition.x = 0
        if (selectedPosition.y < 0)
            selectedPosition.y = 0

    }

    private fun getAdjacentCells(position: IntVector2): List<Cell> {
        val adjCells = ArrayList<Cell>()
        val i = position.x
        val j = position.y
        if (cellExistsAt(i - 1, j))
            adjCells.add(_cellArray[i - 1][j]!!)
        if (cellExistsAt(i + 1, j))
            adjCells.add(_cellArray[i + 1][j]!!)
        if (cellExistsAt(i, j + 1))
            adjCells.add(_cellArray[i][j + 1]!!)
        if (cellExistsAt(i, j - 1))
            adjCells.add(_cellArray[i][j - 1]!!)

        if (i % 2 != 0) {
            if (cellExistsAt(i - 1, j + 1))
                adjCells.add(_cellArray[i - 1][j + 1]!!)
            if (cellExistsAt(i + 1, j + 1))
                adjCells.add(_cellArray[i + 1][j + 1]!!)
        } else {
            if (cellExistsAt(i - 1, j - 1))
                adjCells.add(_cellArray[i - 1][j - 1]!!)
            if (cellExistsAt(i + 1, j - 1))
                adjCells.add(_cellArray[i + 1][j - 1]!!)
        }

        return adjCells
    }

    private fun findOffsetForIndex(idx: IntVector2): Vector2 {
        return Vector2(-hexagonWidth / 4,
                if (idx.x % 2 == 0) 0f else hexagonHeight / 2)
    }

    private fun cellExistsAt(i: Int, j: Int): Boolean {
        return cellExistsAt(IntVector2(i, j))
    }

    private fun cellExistsAt(position: IntVector2): Boolean {

        return if (position.x < 0 || position.y < 0 || position.x >= _cellArray.size || position.y >= _cellArray[0].size) false else getCellByIndex(position) != null
    }

    override fun getPrefWidth(): Float {
        return _cellArray.size.toFloat() * hexagonWidth * 0.75f + hexagonWidth / 4
    }

    override fun getPrefHeight(): Float {
        return _cellArray[0].size * hexagonHeight + hexagonHeight / 2
    }

    fun getCellArray(): Array<Array<Cell?>> {
        return _cellArray
    }

    fun setCellArray(newCellArray: Array<Array<Cell?>>) {
        this._cellArray = newCellArray
        selectedPosition = IntVector2(0, 0)
        selectedCell = newCellArray[0][0]
        if (newCellArray.size > 9) {
            elementHeight = 45f
            elementWidth = 54f
            hexagonHeight = 121f
            hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio
        } else if (newCellArray.size > 6) {
            elementHeight = 70f
            elementWidth = 70f
            hexagonHeight = 188f
            hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio
        } else if (newCellArray.size > 4) {
            elementHeight = 90f
            elementWidth = 90f
            hexagonHeight = 242f
            hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio
        } else {
            elementHeight = 130f
            elementWidth = 130f
            hexagonHeight = 350f
            hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio
        }
    }

    fun addElementToSelectedCell(element: Element) {
        if (selectedCell == null) {
            errorSound.play(soundVolume)
            return
        }
        ElementFunctions.addElementToCell(selectedCell!!, element)
    }

    fun removeOrCreateCell() {
        if (selectedCell == null) {
            _cellArray[selectedPosition.x][selectedPosition.y] = CellFactory.generateEmptyCell(selectedPosition)
            selectedCell = _cellArray[selectedPosition.x][selectedPosition.y]
        } else {
            selectedCell = null
            _cellArray[selectedPosition.x][selectedPosition.y] = null
            selectedPosition = IntVector2(0, 0)
        }
    }

    fun setHexSize(size: IntVector2) {
        if (size.x < 1)
            size.x = 1
        if (size.y < 1)
            size.y = 1
        val newCellArray = Array(size.x) { Array<Cell?>(size.y) { null } }
        val width = if (size.x > _cellArray.size) _cellArray.size else size.x
        val height = if (size.y > _cellArray[0].size) _cellArray[0].size else size.y
        for (i in 0 until width) {
            for (j in 0 until height) {
                newCellArray[i][j] = _cellArray[i][j]
            }
        }
        setCellArray(newCellArray)
        selectedPosition = IntVector2(0, 0)
        selectedCell = _cellArray[0][0]
    }
}
