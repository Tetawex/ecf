package org.tetawex.ecf.presentation.tutorial

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.utils.OrderedMap
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.model.Element
import org.tetawex.ecf.util.IntVector2
import org.tetawex.ecf.util.MathUtils
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by tetawex on 02.05.17.
 */
@Deprecated("")
class OldTutorialHexMapWidget(val game: ECFGame) : Widget() {

    var cellArray: Array<Array<Cell?>>
    var unlockedCells = Array(4) { BooleanArray(4) }
    var fromCell = IntVector2(1, 3)
    var toCell = IntVector2(0, 4)

    var cellActionListener: TutorialCellActionListener? = null
    var acceptAnyClick = true

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

    private val hexagonWidth: Float
    private val hexagonHeight: Float

    private val elementWidth = 130f
    private val elementHeight = 130f

    private var selectedCell: Cell? = null

    private val cellRegion: TextureRegion
    private val selectedRegion: TextureRegion
    private val adjacentRegion: TextureRegion
    private val disabledRegion: TextureRegion

    private val soundVolume: Float

    private val clickSound: Sound
    private val errorSound: Sound
    private val mergeSound: Sound

    private val textureToElementMap: OrderedMap<Element, TextureRegion>

    interface TutorialCellActionListener {
        fun cellMerged(mergedElementsCount: Int)

        fun cellMoved(cellElementCount: Int)

        fun canMove(cellElementCount: Int): Boolean
    }

    init {

        soundVolume = PreferencesProvider.getPreferences().soundVolume

        cellRegion = game.getTextureRegionFromAtlas("hexagon")
        selectedRegion = game.getTextureRegionFromAtlas("hexagon_selected")
        adjacentRegion = game.getTextureRegionFromAtlas("hexagon_adjacent")
        disabledRegion = game.getTextureRegionFromAtlas("hexagon_disabled")

        clickSound = game.assetManager.get("sounds/click.ogg", Sound::class.java)
        errorSound = game.assetManager.get("sounds/error.ogg", Sound::class.java)
        mergeSound = game.assetManager.get("sounds/merge.ogg", Sound::class.java)

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

        hexagonHeight = 350f
        hexagonWidth = hexagonHeight * MathUtils.hexagonWidthToHeightRatio

        cellArray = Array<Array<Cell?>>(0) { arrayOfNulls<Cell?>(0) }

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
                if (acceptAnyClick)
                    cellActionListener!!.cellMoved(0)
                processIndexClick(getCellIndexByVector(Vector2(x, y)))
                return true
            }
        })
    }

    override fun act(deltaTime: Float) {}

    override fun draw(batch: Batch?, parentAlpha: Float) {
        //draw hexagons
        for (i in cellArray.indices) {
            for (j in 0 until cellArray[i].size) {
                if (cellExistsAt(i, j)) {
                    val vec = cellArray[i][j]!!!!.position
                    val offset = findOffsetForIndex(vec!!)
                    batch!!.draw(cellRegion, x + vec.x * (hexagonWidth + offset.x), y + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight)
                }
            }
        }
        //draw selected cells
        if (selectedCell != null) {
            val vec = selectedCell!!.position
            val offset = findOffsetForIndex(vec!!)
            batch!!.draw(selectedRegion, x + vec.x * (hexagonWidth + offset.x), y + vec.y * hexagonHeight + offset.y,
                    hexagonWidth, hexagonHeight)
            for (cell in getAdjacentCells(vec)) {
                val vec2 = cell!!.position
                val offset2 = findOffsetForIndex(vec2!!)
                batch.draw(adjacentRegion, x + vec2.x * (hexagonWidth + offset2.x), y + vec2.y * hexagonHeight + offset2.y,
                        hexagonWidth, hexagonHeight)
            }

        }
        //draw elements
        for (i in cellArray.indices) {
            for (j in 0 until cellArray[i].size) {
                if (cellExistsAt(i, j)) {
                    val vec = cellArray[i][j]!!.position
                    val offset = findOffsetForIndex(vec!!)
                    val cell = cellArray[i][j]!!
                    val elements = cell.elements!!.orderedItems()
                    val size = cell.elements!!.size
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
                    } else
                        batch!!.draw(textureToElementMap.get(Element.EARTH), (x
                                + i * (hexagonWidth + offset.x) + hexagonWidth / 2) - elementWidth / 2,
                                y + j * hexagonHeight + offset.y + hexagonHeight / 2 - 3 / 2,
                                3f, 3f)
                }
            }
        }
        for (i in cellArray.indices) {
            for (j in 0 until cellArray[i].size) {
                if (cellExistsAt(i, j) && !unlockedCells[i][j]) {
                    val vec = cellArray[i][j]!!!!.position
                    val offset = findOffsetForIndex(vec!!)
                    batch!!.draw(disabledRegion, x + vec.x * (hexagonWidth + offset.x), y + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight)
                }
            }
        }
    }

    private fun getCellIndexByVector(vector: Vector2): IntVector2 {
        val i = ((vector.x - hexagonWidth * 0.125f) / (hexagonWidth * 0.75f)).toInt()
        val j = if (i % 2 == 0)
            (vector.y / hexagonHeight).toInt()
        else
            ((vector.y - hexagonHeight * 0.5f) / hexagonHeight).toInt()
        return IntVector2(i, j)
    }

    private fun getCellByIndex(vector: IntVector2): Cell? {
        return cellArray[vector.x][vector.y]
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
        if (!cellExistsAt(position))
            return
        val cell = getCellByIndex(position)
        if (selectedCell == null) {
            selectedCell = cell
            if (!(cell!!.position!!.x == fromCell.x && cell.position!!.y == fromCell.y)) {
                selectedCell = null
                errorSound.play(soundVolume)
                return
            }
            clickSound.play(soundVolume)
        } else {
            if (cell != null) {
                if (cell !== selectedCell) {
                    if (cell.position!!.x == toCell.x && cell.position!!.y == toCell.y) {
                        cellActionListener!!.cellMoved(selectedCell!!.elements!!.size)
                        cellActionListener!!.cellMerged(cell.interactWith(selectedCell!!))

                        selectedCell = null
                        mergeSound.play(soundVolume)
                    }

                } else {
                    selectedCell = null
                }

            }
        }
    }

    private fun getAdjacentCells(position: IntVector2): List<Cell?> {
        val adjCells = ArrayList<Cell?>()
        val i = position.x
        val j = position.y
        if (cellExistsAt(i - 1, j))
            adjCells.add(cellArray[i - 1][j])
        if (cellExistsAt(i + 1, j))
            adjCells.add(cellArray[i + 1][j])
        if (cellExistsAt(i, j + 1))
            adjCells.add(cellArray[i][j + 1])
        if (cellExistsAt(i, j - 1))
            adjCells.add(cellArray[i][j - 1])

        if (i % 2 != 0) {
            if (cellExistsAt(i - 1, j + 1))
                adjCells.add(cellArray[i - 1][j + 1])
            if (cellExistsAt(i + 1, j + 1))
                adjCells.add(cellArray[i + 1][j + 1])
        } else {
            if (cellExistsAt(i - 1, j - 1))
                adjCells.add(cellArray[i - 1][j - 1])
            if (cellExistsAt(i + 1, j - 1))
                adjCells.add(cellArray[i + 1][j - 1])
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

        return if (position.x < 0 || position.y < 0 || position.x >= cellArray.size || position.y >= cellArray[0].size) false else getCellByIndex(position) != null
    }

    override fun getPrefWidth(): Float {
        return cellArray.size.toFloat() * hexagonWidth * 0.75f + hexagonWidth / 4
    }

    override fun getPrefHeight(): Float {
        return cellArray[0].size * hexagonHeight + hexagonHeight / 2
    }

    fun lockCells() {
        unlockedCells = Array(4) { BooleanArray(4) }
    }

    fun unlockCells() {
        for (i in unlockedCells.indices) {
            for (j in 0 until unlockedCells[i].size) {
                unlockedCells[i][j] = true
            }
        }
    }
}
