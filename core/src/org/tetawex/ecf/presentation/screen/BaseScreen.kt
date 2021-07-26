package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import org.tetawex.ecf.core.ECFGame

/**
 * Created by Tetawex on 17.05.2017.
 */
open class BaseScreen(val game: ECFGame) : ScreenAdapter() {
    val stage = createBaseStage()

    init {
        Gdx.input.inputProcessor = stage
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    override fun dispose() {
        stage.dispose()
    }
}
