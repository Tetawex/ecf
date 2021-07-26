package org.tetawex.ecf.presentation.widget

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.presentation.BUTTON_FONT_SCALE
import org.tetawex.ecf.presentation.BUTTON_HEIGHT
import org.tetawex.ecf.presentation.BUTTON_WIDTH
import org.tetawex.ecf.presentation.StyleFactory

class OutlineTextButton(text: String, game: ECFGame) :
    TextButton(text, StyleFactory.generateStandardMenuButtonStyle(game)) {
    init {
        label.setFontScale(BUTTON_FONT_SCALE)
    }

    override fun getPrefWidth(): Float {
        return BUTTON_WIDTH
    }

    override fun getPrefHeight(): Float {
        return BUTTON_HEIGHT
    }
}
