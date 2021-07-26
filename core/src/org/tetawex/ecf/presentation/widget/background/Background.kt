package org.tetawex.ecf.presentation.widget.background

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.tetawex.ecf.core.ECFGame

class Background(game: ECFGame, levelCode: String) : Image(
    game.assetManager
        .get("backgrounds/" + levelCode + "background.png", Texture::class.java)
) {
    init {
        setFillParent(true)
    }
}