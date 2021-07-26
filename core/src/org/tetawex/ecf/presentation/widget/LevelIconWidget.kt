package org.tetawex.ecf.presentation.widget

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.LevelCompletionState

/**
 * Created by Tetawex on 06.06.2017.
 */
class LevelIconWidget(
    val game: ECFGame,
    private val levelCompletionState: LevelCompletionState,
    private val font: BitmapFont, private val number: Int
) : Widget() {
    private val starRegion: TextureRegion
    private val starDisabledRegion: TextureRegion
    private val backgroundRegion: TextureRegion
    private val tintRegion: TextureRegion
    private val starSize = 80f

    init {
        starRegion = game.getTextureRegionFromAtlas("star")
        starDisabledRegion = game.getTextureRegionFromAtlas("star_ungained")
        backgroundRegion = game.getTextureRegionFromAtlas("level_active")
        tintRegion = game.getTextureRegionFromAtlas("level_inactive")
    }

    override fun act(deltaTime: Float) {}

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(backgroundRegion, x, y, 400f, 400f)
        if (number < 10)
            font.draw(batch, number.toString() + "", x + width / 2 - 20, y + height / 2 + 40f)
        else
            font.draw(batch, number.toString() + "", x + width / 2 - 40, y + height / 2 + 40f)
        if (levelCompletionState.completed) {
            for (i in 1..3) {
                if (levelCompletionState.stars >= i)
                    batch.draw(starRegion, 80f + x + starSize * (i - 1), 60 + y, starSize, starSize)
                else
                    batch.draw(
                        starDisabledRegion,
                        80f + x + starSize * (i - 1),
                        60 + y,
                        starSize,
                        starSize
                    )
            }
        }
        if (!levelCompletionState.unlocked) {
            batch.draw(tintRegion, x, y, 400f, 400f)
        }
    }

    override fun getPrefWidth(): Float {
        return 400f
    }

    override fun getPrefHeight(): Float {
        return 400f
    }
}
