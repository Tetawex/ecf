package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

class LoadingScreen(game: ECFGame, bundle: Bundle?) : BaseScreen(game) {
    private val preferences: ECFPreferences

    init {
        preferences = PreferencesProvider.getPreferences()
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

    override fun show() {

    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }
}
