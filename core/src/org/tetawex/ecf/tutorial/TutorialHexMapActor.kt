package org.tetawex.ecf.tutorial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Touchable
import org.tetawex.ecf.presentation.actor.HexMapActor
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.util.IntVector2

/**
 * Created by tetawex on 21.08.17.
 */
class TutorialHexMapActor(game: ECFGame) : HexMapActor(game) {

    var unlockedCells = Array(1) { BooleanArray(1) }
    var fromCell = IntVector2(2, 0)
    var toCell = IntVector2(2, 1)

    var acceptAnyClick = true

    private val disabledRegion: TextureRegion

    init {

        disabledRegion = game.getTextureRegionFromAtlas("hexagon_disabled")

        touchable = Touchable.enabled
        listeners.clear()
        addListener(object : InputListener() {
            override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                 pointer: Int, button: Int) {
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {}

            override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                   pointer: Int, button: Int): Boolean {
                if (acceptAnyClick)
                    cellActionListener.cellMoved(0)
                processIndexClick(getCellIndexByVector(Vector2(x, y)))
                return true
            }
        })
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        for (i in _cellArray.indices) {
            for (j in 0 until _cellArray[i].size) {
                if (cellExistsAt(i, j) && !unlockedCells[i][j]) {
                    val vec = _cellArray[i][j]!!.position
                    val offset = findOffsetForIndex(vec)
                    batch!!.draw(disabledRegion, x + vec!!.x * (hexagonWidth + offset.x), y + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight)
                }
            }
        }
    }

    override fun processIndexClick(position: IntVector2) {
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
            //Gdx.app.log("cell", selectedCell.getPosition().x+"");
            clickSound.play(soundVolume)
        } else {
            if (cell != null) {
                if (cell !== selectedCell) {
                    if (cell.position!!.x == toCell.x && cell.position!!.y == toCell.y) {
                        cellActionListener.cellMoved(selectedCell!!.elements!!.size)
                        cellActionListener.cellMerged(cell.interactWith(selectedCell!!))

                        selectedCell = null
                        mergeSound.play(soundVolume)
                    }

                } else {
                    selectedCell = null
                }

            }
        }
        Gdx.app.log("cell", (selectedCell == null).toString() + "")
    }

    override fun setCellArray(cellArray: Array<Array<Cell?>>) {
        this._cellArray = cellArray
        unlockedCells = Array(_cellArray.size) { BooleanArray(_cellArray[0].size) }
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
