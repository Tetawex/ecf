package org.tetawex.ecf.presentation.widget.background

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.tetawex.ecf.core.ECFGame

class Background(game: ECFGame, assetName: String) : Image(
    game.assetManager
        .get("backgrounds/" + assetName + "background.png", Texture::class.java)
) {
    constructor(game: ECFGame) : this(game, "")

    init {
        setFillParent(true)
    }
}