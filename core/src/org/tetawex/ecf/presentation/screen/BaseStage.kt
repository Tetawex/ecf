package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.presentation.SCREEN_HEIGHT
import org.tetawex.ecf.presentation.SCREEN_WIDTH

fun createBaseStage(): Stage {
    val camera = OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT)
    camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)

    return Stage(ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera))
}
