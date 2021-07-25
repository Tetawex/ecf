package org.tetawex.ecf.tutorial

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.model.CellArrayFactory
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.GameData
import org.tetawex.ecf.presentation.screen.BaseScreen
import org.tetawex.ecf.presentation.screen.StyleFactory
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.IntVector2
import org.tetawex.ecf.util.PreferencesProvider

/**
 * ...
 */
class TutorialScreen(game: ECFGame, bundle: Bundle?) : BaseScreen<ECFGame>(game) {

    private val gameStage: Stage
    private var hexMapActor: OldTutorialHexMapActor? = null
    private var scoreLabel: Label? = null
    private var manaLabel: TextButton? = null

    private var tutButton: TextButton? = null

    private val gameData: GameData

    private var tutorialStage = 0

    private val preferences: ECFPreferences

    private var backgroundPause: Image? = null
    private var pauseTable: Table? = null

    init {
        preferences = PreferencesProvider.getPreferences()
        preferences.completedTutorial = true
        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        gameStage = Stage(ExtendViewport(1440f, 2560f, camera))

        Gdx.input.inputProcessor = gameStage

        gameData = GameData()

        initUi()

        gameData.cellArray = CellArrayFactory.generateTutorialCellArray()
        gameData.setMana(2)
        gameData.setScore(0)
        gameData.maxScore = 10000

    }

    override fun render(delta: Float) {
        gameStage.act()
        gameStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, true)
        gameStage.viewport.camera.update()
    }

    private fun initUi() {
        pauseTable = Table()
        pauseTable!!.isVisible = false

        val mainTable = Table()
        val stack = Stack()
        stack.setFillParent(true)

        val background = Image(game.assetManager.get("backgrounds/background.png", Texture::class.java))
        background.setFillParent(true)
        backgroundPause = Image(game.assetManager.get("backgrounds/background_pause.png", Texture::class.java))
        backgroundPause!!.setFillParent(true)
        backgroundPause!!.isVisible = false

        stack.add(background)
        stack.add(mainTable)
        stack.add(backgroundPause)
        stack.add(pauseTable)

        gameStage.addActor(stack)

        val topRowTable = Table()
        val topRowLeftTable = Table()
        val topRowCenterTable = Table()
        val topRowRightTable = Table()
        topRowTable.add(topRowLeftTable).width(300f)
        topRowTable.add(topRowCenterTable).growX()
        topRowTable.add(topRowRightTable).width(300f)
        val midRowTable = Table()
        val bottomRowTable = Table()

        hexMapActor = OldTutorialHexMapActor(game)
        hexMapActor!!.cellActionListener = object : OldTutorialHexMapActor.TutorialCellActionListener {
            override fun cellMerged(mergedElementsCount: Int) {
                gameData.processElementsMerge(mergedElementsCount)
            }

            override fun cellMoved(cellElementCount: Int) {
                gameData.spendManaOnMove(cellElementCount)
                advanceTutorial()
            }

            override fun canMove(cellElementCount: Int): Boolean {
                return gameData.canMove(cellElementCount)
            }
        }
        midRowTable.add<OldTutorialHexMapActor>(hexMapActor).center().expand().padTop(60f)

        mainTable.setFillParent(true)
        mainTable.add(topRowTable).growX().row()
        mainTable.add(midRowTable).growX().growY().row()
        mainTable.add(bottomRowTable).growX().prefHeight(600f)

        val tutStack = Stack()
        tutButton = TextButton(game.getLocalisedString("tutorial0"), StyleFactory.generateStandardTutorialButtonStyle(game))
        tutButton!!.label.setWrap(true)
        bottomRowTable.add<TextButton>(tutButton).pad(PAUSE_BUTTON_PAD).width(PAUSE_BUTTON_WIDTH).grow()

        val pauseButton = TextButton(" ", StyleFactory.generatePauseButtonStyle(game))
        pauseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable!!.isVisible = !pauseTable!!.isVisible
                backgroundPause!!.isVisible = !backgroundPause!!.isVisible
            }
        })
        topRowLeftTable.left().top()
        topRowLeftTable.add(pauseButton).size(120f).pad(40f).center()

        scoreLabel = Label("", StyleFactory.generateStandardLabelStyle(game))
        scoreLabel!!.setFontScale(SCORE_LABEL_FONT_SCALE)
        topRowCenterTable.add<Label>(scoreLabel)

        val topRowRightStack = Stack()
        manaLabel = TextButton("", StyleFactory.generateManaButtonStyle(game))
        topRowRightTable.add<TextButton>(manaLabel).size(150f).pad(40f)

        topRowTable.toFront()
        topRowRightTable.right()
        topRowLeftTable.left()

        gameData.gameDataChangedListener = object : GameData.GameDataChangedListener {
            override fun manaChanged(newValue: Int) {
                manaLabel!!.setText(newValue.toString() + "")
            }

            override fun scoreChanged(newValue: Int) {
                scoreLabel!!.setText(newValue.toString() + "")
            }

            override fun cellMapChanged(newMap: Array<Array<Cell?>>) {
                hexMapActor!!.cellArray = newMap
            }

            override fun elementsCountChanged(fire: Int, water: Int, air: Int, earth: Int, shadow: Int, light: Int, time: Int) {

            }

            override fun gameLostOrWon(payload: GameData.WinLossPayload) {
            }
        }
        //pause ui
        pauseTable!!.center()

        val pauseMenuButtonContinue = TextButton(game.getLocalisedString("continue"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonContinue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable!!.isVisible = !pauseTable!!.isVisible
                backgroundPause!!.isVisible = !backgroundPause!!.isVisible
            }
        })
        pauseMenuButtonContinue.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable!!.add(pauseMenuButtonContinue)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()

        val pauseMenuButtonQuit = TextButton(game.getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable!!.isVisible = !pauseTable!!.isVisible
                backgroundPause!!.isVisible = !backgroundPause!!.isVisible
                val bundle = Bundle()
                bundle.putItem("levelCode", "")
                game.gameStateManager.setState(GameStateManager.GameState.MAIN_MENU, bundle)
            }
        })
        pauseMenuButtonQuit.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable!!.add(pauseMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()
    }

    fun advanceTutorial() {
        tutorialStage++
        if (tutorialStage == 2) {
            hexMapActor!!.unlockedCells[2][1] = true
            hexMapActor!!.unlockedCells[3][0] = true
            hexMapActor!!.fromCell = IntVector2(2, 1)
            hexMapActor!!.toCell = IntVector2(3, 0)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 3) {
            hexMapActor!!.lockCells()
            hexMapActor!!.unlockedCells[3][2] = true
            hexMapActor!!.unlockedCells[3][3] = true
            hexMapActor!!.fromCell = IntVector2(3, 3)
            hexMapActor!!.toCell = IntVector2(3, 2)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 4) {
            hexMapActor!!.lockCells()
            hexMapActor!!.unlockedCells[3][1] = true
            hexMapActor!!.unlockedCells[2][2] = true
            hexMapActor!!.unlockedCells[2][3] = true
            hexMapActor!!.fromCell = IntVector2(2, 3)
            hexMapActor!!.toCell = IntVector2(2, 2)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 5) {
            hexMapActor!!.unlockedCells[2][3] = false
            hexMapActor!!.fromCell = IntVector2(2, 2)
            hexMapActor!!.toCell = IntVector2(3, 1)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 6) {
            hexMapActor!!.lockCells()
            hexMapActor!!.unlockedCells[1][2] = true
            hexMapActor!!.unlockedCells[1][1] = true
            hexMapActor!!.fromCell = IntVector2(1, 2)
            hexMapActor!!.toCell = IntVector2(1, 1)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 7) {
            hexMapActor!!.lockCells()
            hexMapActor!!.unlockedCells[0][2] = true
            hexMapActor!!.unlockedCells[1][1] = true
            hexMapActor!!.fromCell = IntVector2(0, 2)
            hexMapActor!!.toCell = IntVector2(1, 1)
            hexMapActor!!.acceptAnyClick = false
        } else if (tutorialStage == 8) {
            hexMapActor!!.unlockCells()
            hexMapActor!!.fromCell = IntVector2(1000, 1000)
            hexMapActor!!.toCell = IntVector2(1000, 1000)
            hexMapActor!!.acceptAnyClick = true
        } else if (tutorialStage == 9) {
            hexMapActor!!.unlockCells()
            hexMapActor!!.fromCell = IntVector2(1000, 1000)
            hexMapActor!!.toCell = IntVector2(1000, 1000)
            hexMapActor!!.acceptAnyClick = true
        } else if (tutorialStage == 10) {
            val bundle = Bundle()
            bundle.putItem("levelCode", "")
            game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
            return
        }
        tutButton!!.setText(game.getLocalisedString("tutorial$tutorialStage"))
    }

    override fun onBackPressed(): Boolean {
        if (pauseTable!!.isVisible) {
            pauseTable!!.isVisible = false
            backgroundPause!!.isVisible = false
        } else {
            pauseTable!!.isVisible = true
            backgroundPause!!.isVisible = true
        }
        return true
    }

    companion object {
        private val PAUSE_BUTTON_WIDTH = 1275f
        private val PAUSE_BUTTON_HEIGHT = 252f
        private val PAUSE_BUTTON_PAD = 40f
        private val PAUSE_BUTTON_FONT_SCALE = 1f
        private val MANA_LABEL_FONT_SCALE = 1f
        private val SCORE_LABEL_FONT_SCALE = 1f
    }
}
