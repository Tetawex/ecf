package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.util.Bundle

class LoadingScreen(game: ECFGame, bundle: Bundle?) : BaseScreen(game) {
    init {
        initUi()
    }

    private fun initUi() {
        val image = Image(
            game.assetManager.get(
                "backgrounds/background_stub.png",
                Texture::class.java
            )
        )
        image.setFillParent(true)

        stage.addActor(image)
    }
}
