package org.tetawex.ecf.presentation.widget.background

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.tetawex.ecf.core.ECFGame

class PauseBackground(game: ECFGame) : Image(
    game.assetManager
        .get("backgrounds/background_pause.png", Texture::class.java)
) {
    init {
        setFillParent(true)
    }
}
