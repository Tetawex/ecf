package org.tetawex.ecf.presentation.widget.background

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.tetawex.ecf.core.ECFGame

open class Background(game: ECFGame, assetName: String) : Image(
    game.assetManager
        .get("backgrounds/$assetName.png", Texture::class.java)
) {
    init {
        setFillParent(true)
    }
}