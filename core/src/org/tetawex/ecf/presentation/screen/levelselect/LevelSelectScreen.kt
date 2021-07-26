package org.tetawex.ecf.presentation.screen.levelselect

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import org.tetawex.ecf.presentation.widget.PagedScrollPane
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.LevelFactory
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.presentation.screen.BaseScreen
import org.tetawex.ecf.presentation.screen.StyleFactory
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider
import kotlin.math.ceil
import kotlin.math.round

/**
 * Created by Tetawex on 03.06.2017.
 */

const val SHOULD_RESTORE_SCROLL_POSITION_KEY = "shouldRestoreScrollPosition"

class LevelSelectScreen(game: ECFGame, bundle: Bundle?, savedState: Bundle?) :
    BaseScreen(game) {

    private val preferences: ECFPreferences

    private val levelCode: String
    private val initialScrollPosition: Float

    private lateinit var scroll: PagedScrollPane

    init {
        preferences = PreferencesProvider.getPreferences()

        levelCode = bundle!!.getItem("levelCode", String::class.java, "")!!
        val shouldRestoreScrollPosition =
            bundle.getItem(SHOULD_RESTORE_SCROLL_POSITION_KEY, String::class.java, "false")
                ?: "false"

        initialScrollPosition =
            (VIEWPORT_WIDTH + 2 * DEFAULT_PADDING) * (if (shouldRestoreScrollPosition.toBoolean()) {
                (savedState?.getItem("page", String::class.java, "0") ?: "1").toFloat()
            } else {
                0f
            })

        initUi()
    }

    private fun initUi() {
        val stack = Stack()
        stack.setFillParent(true)
        stack.add(
            Image(
                game.assetManager
                    .get(
                        "backgrounds/" + levelCode + "background.png",
                        Texture::class.java
                    )
            )
        )
        val mainTable = Table()
        mainTable.setFillParent(true)
        stack.add(mainTable)
        stage.addActor(stack)

        scroll = PagedScrollPane()
        scroll.setFlingTime(10f)
        scroll.setPageSpacing(0f)

        val pageCount =
            ceil((preferences.getLevelCompletionStateList(levelCode)!!.size / 9f).toDouble()).toInt()
        var i = 0
        outerLoop@ for (l in 0 until pageCount) {
            val levelTable = Table()
            if (l == 0)
                levelTable.padTop(DEFAULT_PADDING_HALF).padBottom(DEFAULT_PADDING_HALF).padLeft(DEFAULT_PADDING).padRight(DEFAULT_PADDING)
            else if (l == pageCount - 1)
                levelTable.padTop(DEFAULT_PADDING_HALF).padBottom(DEFAULT_PADDING_HALF).padLeft(DEFAULT_PADDING_HALF).padRight(DEFAULT_PADDING)
            else
                levelTable.pad(DEFAULT_PADDING_HALF)

            levelTable.defaults().pad(0f, 0f, 0f, 0f)
            levelTable.align(Align.top)

            scroll.addPage(levelTable)
            for (y in 0 until LEVEL_ROWS) {
                levelTable.row()
                for (x in 0 until LEVEL_COLUMNS) {
                    if (i >= preferences.getLevelCompletionStateList(levelCode)!!.size) {
                        levelTable.row()
                        break@outerLoop
                    }
                    val actor = LevelIconWidget(
                        game,
                        preferences.getLevelCompletionStateList(levelCode)!![i],
                        game.assetManager.get("fonts/font_main_medium.ttf", BitmapFont::class.java),
                        i + 1
                    )
                    val act = levelTable.add(actor).right().padBottom(LEVEL_ICON_PADDING)
                    if (x != 2) {
                        act.padRight(LEVEL_ICON_PADDING)
                    }

                    val finalI = i
                    actor.addListener(
                        object : InputListener() {
                            internal var touchX: Float = 0.toFloat()
                            internal var touchY: Float = 0.toFloat()

                            override fun touchUp(
                                event: InputEvent?, x: Float, y: Float,
                                pointer: Int, button: Int
                            ) {
                                if ((x - touchX) * (x - touchX) + (y - touchY) * (y - touchY) < 2000f && preferences.getLevelCompletionStateList(
                                        levelCode
                                    )!![finalI].unlocked
                                ) {
                                    val bundle = Bundle()
                                    bundle.putItem(
                                        "levelData",
                                        LevelFactory.generateLevel(finalI, levelCode)
                                    )
                                    game.gameStateManager.setState(
                                        GameStateManager.GameState.GAME,
                                        bundle
                                    )
                                }
                            }

                            override fun touchDragged(
                                event: InputEvent?,
                                x: Float,
                                y: Float,
                                pointer: Int
                            ) {
                            }

                            override fun touchDown(
                                event: InputEvent?, x: Float, y: Float,
                                pointer: Int, button: Int
                            ): Boolean {
                                touchX = x
                                touchY = y
                                return true
                            }


                        })
                    i++
                }
            }
        }
        mainTable.add(scroll).expand().fillX().row()
        scroll.height = 420f
        scroll.scrollTo(initialScrollPosition, 0f, 0f, 0f)


        val menuButtonBackToMainMenu = TextButton(
            game.getLocalisedString("back"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
            }
        })
        menuButtonBackToMainMenu.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD)
            .align(Align.top)
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
        game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
        return true
    }

    override fun dispose() {
        val savedState = Bundle()
        savedState.putItem("page", round(scroll.scrollX / VIEWPORT_WIDTH).toString())

        game.gameStateManager.saveStateForGameState(
            GameStateManager.GameState.LEVEL_SELECT,
            savedState
        )

        super.dispose()
    }
}
