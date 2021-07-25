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
import org.tetawex.ecf.model.LevelData
import org.tetawex.ecf.model.RandomLevelFactory
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

import java.util.Random

/**
 * Created by Tetawex on 03.06.2017.
 */
class PlayModeSelectScreen(game: ECFGame, bundle: Bundle?) : BaseScreen<ECFGame>(game) {

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
        val menuButtonLevels = TextButton(game.getLocalisedString("levels"), tbStyle)
        menuButtonLevels.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.LEVEL_PACK_SELECT, null)
            }
        })
        val menuButtonRandom = TextButton(game.getLocalisedString("random"), tbStyle)
        menuButtonRandom.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val bundle = Bundle()
                val factory = RandomLevelFactory(Random().nextInt(10000), Random().nextInt(2) + 3, Random().nextInt(3) + 3)
                bundle.putItem("levelData", LevelData(factory.theBoard, factory.mana, 10000, -1, "Random Level", "random"))
                game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
            }
        })
        val menuButtonTutorial = TextButton(game.getLocalisedString("how_to_play"), tbStyle)
        menuButtonTutorial.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.TUTORIAL, null)
            }
        })
        val menuButtonEditor = TextButton(game.getLocalisedString("editor"), tbStyle)
        menuButtonEditor.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.EDITOR, null)
            }
        })
        val menuButtonBackToMainMenu = TextButton(game.getLocalisedString("back"), tbStyle)
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        mainTable.add(menuButtonLevels).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonRandom).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonTutorial).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonEditor).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row()
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    override fun onBackPressed(): Boolean {
        game.gameStateManager.setState(GameStateManager.GameState.MAIN_MENU, null)
        return true
    }

    companion object {
        private val BUTTON_WIDTH = 1275f
        private val BUTTON_HEIGHT = 255f
        private val BUTTON_PAD = 40f
        private val BUTTON_FONT_SCALE = 1f
        private val LABEL_FONT_SCALE = 1f
    }
}
