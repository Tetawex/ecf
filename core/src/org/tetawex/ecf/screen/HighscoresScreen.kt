package org.tetawex.ecf.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.Score
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class HighscoresScreen(game: ECFGame, bundle: Bundle?) : BaseScreen<ECFGame>(game) {

    private val preferences: ECFPreferences

    private val stage: Stage

    init {
        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        stage = Stage(ExtendViewport(1440f, 2560f, camera))
        Gdx.input.inputProcessor = stage
        preferences = PreferencesProvider.getPreferences()
        initUi()
    }

    private fun initUi() {
        val stack = Stack()
        stack.setFillParent(true)
        stack.add(Image(
                game.assetManager
                        .get("backgrounds/background.png",
                                Texture::class.java)))
        val mainTable = Table()
        mainTable.setFillParent(true)
        stack.add(mainTable)
        stage.addActor(stack)
        val labelStyle = StyleFactory.generateStandardLabelStyle(game)
        var i = 1
        for (score in preferences.scores) {
            val table = Table()
            table.add(Label(i.toString(), labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f)
            table.add(Label(score.name, labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f).growX()
            table.add(Label(score.value.toString(), labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f)
            mainTable.add(table).growX().row()
            i++
            if (i > 9)
                break
        }
        val menuButtonBackToMainMenu = TextButton(game.getLocalisedString("back"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        menuButtonBackToMainMenu.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD)
    }

    override fun onBackPressed() {
        game.gameStateManager.setState(GameStateManager.GameState.MAIN_MENU, null)
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    companion object {
        private val BUTTON_WIDTH = 1275f
        private val BUTTON_HEIGHT = 255f
        private val BUTTON_PAD = 40f
        private val BUTTON_FONT_SCALE = 1f
        private val LABEL_FONT_SCALE = 1f
    }
}
