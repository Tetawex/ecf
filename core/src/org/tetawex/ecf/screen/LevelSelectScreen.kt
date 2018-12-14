package org.tetawex.ecf.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.actor.LevelIconActor
import org.tetawex.ecf.actor.PagedScrollPane
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.LevelFactory
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class LevelSelectScreen(game: ECFGame, bundle: Bundle?) : BaseScreen<ECFGame>(game) {

    private val preferences: ECFPreferences

    private val stage: Stage
    private val levelCode: String

    init {
        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        stage = Stage(ExtendViewport(1440f, 2560f, camera))
        Gdx.input.inputProcessor = stage
        preferences = PreferencesProvider.getPreferences()
        levelCode = bundle!!.getItem("levelCode", String::class.java, "")!!
        initUi()
    }

    private fun initUi() {
        val stack = Stack()
        stack.setFillParent(true)
        stack.add(Image(
                game.assetManager
                        .get("backgrounds/" + levelCode + "background.png",
                                Texture::class.java)))
        val mainTable = Table()
        mainTable.setFillParent(true)
        stack.add(mainTable)
        stage.addActor(stack)

        val scroll = PagedScrollPane()
        scroll.setFlingTime(10f)
        scroll.setPageSpacing(0f)
        val pageCount = Math.ceil((preferences.getLevelCompletionStateList(levelCode)!!.size / 9f).toDouble()).toInt()
        var i = 0
        outerLoop@ for (l in 0 until pageCount) {
            val levelTable = Table()
            if (l == 0)
                levelTable.padTop(40f).padBottom(40f).padLeft(50f).padRight(25f)
            else if (l == pageCount - 1)
                levelTable.padTop(40f).padBottom(40f).padLeft(25f).padRight(50f)
            else
                levelTable.padTop(40f).padBottom(40f).padLeft(25f).padRight(25f)
            levelTable.defaults().pad(0f, 0f, 0f, 0f)
            scroll.addPage(levelTable)
            for (y in 0..2) {
                levelTable.row()
                for (x in 0..2) {
                    if (i >= preferences.getLevelCompletionStateList(levelCode)!!.size) {
                        levelTable.row()
                        break@outerLoop
                    }
                    val actor = LevelIconActor(game,
                            preferences.getLevelCompletionStateList(levelCode)!![i],
                            game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java),
                            i + 1)
                    levelTable.add(actor).pad(20f).right()
                    val finalI = i
                    actor.addListener(
                            object : InputListener() {
                                internal var touchX: Float = 0.toFloat()
                                internal var touchY: Float = 0.toFloat()

                                override fun touchUp(event: InputEvent?, x: Float, y: Float,
                                                     pointer: Int, button: Int) {
                                    if ((x - touchX) * (x - touchX) + (y - touchY) * (y - touchY) < 2000f && preferences.getLevelCompletionStateList(levelCode)!![finalI].unlocked) {
                                        val bundle = Bundle()
                                        bundle.putItem("levelData", LevelFactory.generateLevel(finalI, levelCode))
                                        game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
                                    }
                                }

                                override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {}

                                override fun touchDown(event: InputEvent?, x: Float, y: Float,
                                                       pointer: Int, button: Int): Boolean {
                                    touchX = x
                                    touchY = y
                                    return true
                                }


                            })
                    i++
                }
            }
        }
        mainTable.add(scroll).expand().fill().row()


        val menuButtonBackToMainMenu = TextButton(game.getLocalisedString("back"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
            }
        })
        menuButtonBackToMainMenu.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD)
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
