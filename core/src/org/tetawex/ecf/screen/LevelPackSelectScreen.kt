package org.tetawex.ecf.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class LevelPackSelectScreen(game: ECFGame, bundle: Bundle?) : BaseECFScreen(game) {

    private val preferences: ECFPreferences

    private val stage: Stage

    init {
        preferences = PreferencesProvider.getPreferences()
        if (!preferences.completedTutorial)
            game.gameStateManager.setState(GameStateManager.GameState.TUTORIAL, null)
        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        stage = Stage(ExtendViewport(1440f, 2560f, camera))
        Gdx.input.inputProcessor = stage
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
        val tbStyle = StyleFactory.generateStandardMenuButtonStyle(game)
        val menuButtonClassic = TextButton(game.getLocalisedString("lp_classic"), tbStyle)
        menuButtonClassic.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val bundle = Bundle()
                bundle.putItem("levelCode", "")
                game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
            }
        })
        val menuButtonMot = TextButton(game.getLocalisedString("lp_mot"), StyleFactory.generateMotMenuButtonStyle(game))
        menuButtonMot.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (preferences.completedMotTutorial) {
                    if (!preferences.completedMotTutorial) {
                        game.gameStateManager.setState(GameStateManager.GameState.MOT_TUTORIAL, null)
                    } else {
                        val bundle = Bundle()
                        bundle.putItem("levelCode", "mot")
                        game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
                    }
                } else
                    game.gameStateManager.setState(GameStateManager.GameState.MOT_TUTORIAL, null)
            }
        })

        val menuButtonSc = TextButton(game.getLocalisedString("lp_sc"), StyleFactory.generateScMenuButtonStyle(game))

        val menuButtonBackPlayModeSelectScreen = TextButton(game.getLocalisedString("back"), tbStyle)
        menuButtonBackPlayModeSelectScreen.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        mainTable.add(menuButtonClassic).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonMot).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        //mainTable.add(menuButtonSc).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonBackPlayModeSelectScreen).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    override fun onBackPressed() {
        game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
    }

    companion object {
        private val BUTTON_WIDTH = 1275f
        private val BUTTON_HEIGHT = 255f
        private val BUTTON_PAD = 40f
        private val BUTTON_FONT_SCALE = 1f
        private val LABEL_FONT_SCALE = 1f
    }
}
