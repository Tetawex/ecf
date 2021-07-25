package org.tetawex.ecf.presentation.screen

import ElementCounterWidget
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.presentation.actor.HexMapActor
import org.tetawex.ecf.presentation.actor.WinLossModal
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.*
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider
import java.util.*

/**
 * ...
 */
class GameScreen(game: ECFGame, bundle: Bundle?) : BaseECFScreen(game) {

    private val levelData: LevelData

    private val gameStage: Stage
    private var hexMapActor: HexMapActor? = null
    private var scoreLabel: Label? = null
    private var manaLabel: TextButton? = null

    private val gameData: GameData

    lateinit var pauseTable: Table
    lateinit var backgroundPause: Image
    lateinit var winLossModal: WinLossModal

    private val preferences: ECFPreferences = PreferencesProvider.getPreferences()

    private val levelCode: String

    init {

        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        gameStage = Stage(ExtendViewport(1440f, 2560f, camera))

        Gdx.input.inputProcessor = gameStage

        gameData = GameData()
        levelData = bundle!!.getItem("levelData", LevelData::class.java)!!
        levelCode = levelData.levelCode!!

        initUi()

        gameData.cellArray = levelData.cellArray
        gameData.setMana(levelData.mana)
        gameData.setScore(0)
        gameData.maxScore = levelData.maxScore
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
        pauseTable.isVisible = false

        val mainTable = Table()
        val stack = Stack()
        stack.setFillParent(true)

        val background = Image(
            game.assetManager
                .get("backgrounds/" + levelCode + "background.png", Texture::class.java)
        )
        background.setFillParent(true)
        backgroundPause = Image(
            game.assetManager
                .get("backgrounds/background_pause.png", Texture::class.java)
        )
        backgroundPause.setFillParent(true)
        backgroundPause.isVisible = false

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
        val elementCounterWidget = ElementCounterWidget(game)

        hexMapActor = HexMapActor(game)
        hexMapActor!!.soundVolume = PreferencesProvider.getPreferences().soundVolume
        hexMapActor!!.cellActionListener = object : HexMapActor.CellActionListener {
            override fun cellMerged(mergedElementsCount: Int) {
                gameData.processElementsMerge(mergedElementsCount)
            }

            override fun cellMoved(cellElementCount: Int) {
                gameData.spendManaOnMove(cellElementCount)
            }

            override fun canMove(cellElementCount: Int): Boolean {
                return gameData.canMove(cellElementCount)
            }
        }
        midRowTable.add<HexMapActor>(hexMapActor).center().expand()

        mainTable.setFillParent(true)
        mainTable.add(topRowTable).growX().row()
        mainTable.add(Label(levelData.name, StyleFactory.generateStandardLabelStyle(game))).row()
        mainTable.add(midRowTable).growX().growY().row()
        mainTable.add(elementCounterWidget).growX()

        val pauseButton = TextButton(" ", StyleFactory.generatePauseButtonStyle(game))
        pauseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
            }
        })
        topRowLeftTable.left().top()
        topRowLeftTable.add(pauseButton).size(120f).pad(40f).center()

        scoreLabel = Label("", StyleFactory.generateStandardLabelStyle(game))
        scoreLabel!!.setFontScale(SCORE_LABEL_FONT_SCALE)
        topRowCenterTable.add<Label>(scoreLabel)

        //Stack topRowRightStack = new Stack();
        manaLabel = TextButton("", StyleFactory.generateManaButtonStyle(game))
        topRowRightTable.add<TextButton>(manaLabel).size(150f).pad(40f)

        topRowTable.toFront()
        topRowRightTable.right()
        topRowLeftTable.left()

        winLossModal = WinLossModal(
            game = game,
            preferences = preferences,
            onNextPressed = { handleNextPressed() },
            onRetryPressed = {
                handleRetryPressed()
            },
            onQuitPressed = {
                handleQuitPressed()
            }
        )
        winLossModal.isVisible = false
        stack.add(winLossModal)

        gameData.gameDataChangedListener = object : GameData.GameDataChangedListener {
            override fun manaChanged(newValue: Int) {
                manaLabel!!.setText(newValue.toString() + "")
            }

            override fun scoreChanged(newValue: Int) {
                scoreLabel!!.setText(newValue.toString() + "")
            }

            override fun cellMapChanged(newMap: Array<Array<Cell?>>) {
                hexMapActor!!.setCellArray(newMap)
            }

            override fun elementsCountChanged(
                fire: Int,
                water: Int,
                air: Int,
                earth: Int,
                shadow: Int,
                light: Int,
                time: Int
            ) {
                elementCounterWidget.setElementCount(
                    fire,
                    water,
                    air,
                    earth,
                    shadow,
                    light,
                    time
                )
            }

            override fun gameLostOrWon(payload: GameData.WinLossPayload) {
                winLossModal.setData(payload)
                backgroundPause.isVisible = true
                winLossModal.isVisible = true

                val totalScore = payload.totals.totalScore
                val starsCount = payload.totals.stars

                if (payload is GameData.WinLossPayload.Win) {
                    if ("random" == levelData.levelCode) {
                        var i = 0
                        val list = preferences.scores
                        for (s in list) {
                            if (totalScore > s.value) {
                                break
                            }
                            i++
                        }
                        if (i < 12)
                            preferences.scores.add(i, Score(totalScore, "Player", levelData.name!!))
                    }
                    if ("editor" != levelData.levelCode && "random" != levelData.levelCode) {
                        preferences
                            .getLevelCompletionStateList(levelCode)!![levelData.levelNumber]
                            .completed = true
                        if (preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber].stars < starsCount) {
                            preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber].stars =
                                starsCount
                        }
                        if (levelData.levelNumber + 1 <
                            PreferencesProvider.getLevelCountForCode(levelCode)
                        ) {
                            preferences
                                .getLevelCompletionStateList(levelCode)!![levelData.levelNumber + 1]
                                .unlocked = true
                        }
                    }
                    PreferencesProvider.flushPreferences()
                }
            }
        }

        //pause ui
        pauseTable.center()

        val pauseMenuButtonContinue = TextButton(
            game.getLocalisedString("continue"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonContinue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {

                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
            }
        })
        pauseMenuButtonContinue.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonContinue)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()

        val pauseMenuButtonRetry = TextButton(
            game.getLocalisedString("retry"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonRetry.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
                gameData.cellArray = levelData.cellArray
                gameData.setScore(0)
                gameData.maxScore = levelData.maxScore
                gameData.setMana(levelData.mana)
            }
        })
        pauseMenuButtonRetry.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonRetry)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()


        val pauseMenuButtonQuit = TextButton(
            game.getLocalisedString("quit"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if ("editor" == levelCode) {
                    goBackToEditor()
                    return
                }
                if ("random" == levelCode) {
                    game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
                    return
                }
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
                if (levelData.levelNumber != -1) {
                    val bundle = Bundle()
                    bundle.putItem("levelCode", levelCode)
                    game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
                } else
                    game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
            }
        })
        pauseMenuButtonQuit.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonQuit)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(PAUSE_BUTTON_PAD).row()
    }

    override fun dispose() {

    }

    override fun onBackPressed(): Boolean {
        if (pauseTable.isVisible) {
            pauseTable.isVisible = false
            backgroundPause.isVisible = false
        } else {
            pauseTable.isVisible = true
            backgroundPause.isVisible = true
        }
        return true
    }

    private fun handleNextPressed() {
        Gdx.app.log("tag", "next")
        when {
            levelCode == "editor" -> {
                goBackToEditor()
                return
            }
            levelCode == "random" -> {
                val bundle = Bundle()
                val factory = RandomLevelFactory(
                    Random().nextInt(10000),
                    Random().nextInt(2) + 3,
                    Random().nextInt(3) + 3
                )
                bundle.putItem(
                    "levelData",
                    LevelData(
                        factory.theBoard,
                        factory.mana,
                        10000,
                        -1,
                        "Random Level",
                        "random"
                    )
                )
                game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
                return
            }
            levelData.levelNumber + 1 >=
                    PreferencesProvider.getLevelCountForCode(levelCode) -> {
                val bundle = Bundle()
                bundle.putItem("levelCode", levelCode)
                game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
            }
            else -> {
                val bundle = Bundle()
                bundle.putItem("levelCode", levelCode)
                bundle.putItem(
                    "levelData",
                    LevelFactory.generateLevel(levelData.levelNumber + 1, levelCode)
                )
                game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
            }
        }
    }

    private fun handleQuitPressed() {
        Gdx.app.log("tag", "q")
        when (levelCode) {
            "editor" -> {
                goBackToEditor()
            }
            "random" -> {
                game.gameStateManager.setState(GameStateManager.GameState.HIGHSCORES, null)
            }
            else -> {
                val bundle = Bundle()
                bundle.putItem("levelCode", levelCode)
                game.gameStateManager.setState(
                    GameStateManager.GameState.LEVEL_SELECT,
                    bundle
                )
            }
        }
    }

    private fun handleRetryPressed() {
        Gdx.app.log("tag", "ret")
        winLossModal.isVisible = false
        backgroundPause.isVisible = false
        gameData.cellArray = levelData.cellArray
        gameData.setScore(0)
        gameData.maxScore = levelData.maxScore
        gameData.setMana(levelData.mana)
    }

    private fun goBackToEditor() {
        val bundle = Bundle()
        bundle.putItem("levelData", levelData)
        game.gameStateManager.setState(GameStateManager.GameState.EDITOR, bundle)
    }
}