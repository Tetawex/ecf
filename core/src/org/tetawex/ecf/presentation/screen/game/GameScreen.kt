package org.tetawex.ecf.presentation.screen.game

import ElementCounterWidget
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import org.tetawex.ecf.presentation.widget.HexMapWidget
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.*
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.presentation.screen.BaseScreen
import org.tetawex.ecf.presentation.screen.levelselect.SHOULD_RESTORE_SCROLL_POSITION_KEY
import org.tetawex.ecf.presentation.StyleFactory
import org.tetawex.ecf.presentation.widget.SafeAreaContainer
import org.tetawex.ecf.presentation.widget.background.LevelBackground
import org.tetawex.ecf.presentation.widget.ScreenContainer
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider
import java.util.*

/**
 * ...
 */
class GameScreen(game: ECFGame, bundle: Bundle?) : BaseScreen(game) {
    private val levelData: LevelData

    private var hexMapWidget: HexMapWidget? = null
    private var scoreLabel: Label? = null
    private var manaLabel: TextButton? = null

    private val gameData: GameData = GameData()

    lateinit var pauseModal: PauseModal
    lateinit var winLossModal: WinLossModal

    private val preferences: ECFPreferences = PreferencesProvider.getPreferences()

    private val levelCode: String

    init {
        levelData = bundle!!.getItem("levelData", LevelData::class.java)!!
        levelCode = levelData.levelCode!!

        initUi()

        gameData.cellArray = levelData.cellArray
        gameData.setMana(levelData.mana)
        gameData.setScore(0)
        gameData.maxScore = levelData.maxScore
    }

    private fun initUi() {
        pauseModal = PauseModal(
            game = game,
            onContinuePressed = {
                pauseModal.isVisible = !pauseModal.isVisible
            },
            onRetryPressed = {
                pauseModal.isVisible = !pauseModal.isVisible

                gameData.cellArray = levelData.cellArray
                gameData.setScore(0)
                gameData.maxScore = levelData.maxScore
                gameData.setMana(levelData.mana)
            },
            onQuitPressed = {
                if ("editor" == levelCode) {
                    goBackToEditor()
                } else if ("random" == levelCode) {
                    game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
                } else {
                    pauseModal.isVisible = !pauseModal.isVisible
                    if (levelData.levelNumber != -1) {
                        val bundle = Bundle()
                        bundle.putItem("levelCode", levelCode)
                        bundle.putItem(SHOULD_RESTORE_SCROLL_POSITION_KEY, "true")
                        game.gameStateManager.setState(
                            GameStateManager.GameState.LEVEL_SELECT,
                            bundle
                        )
                    } else {
                        game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
                    }
                }
            }
        )
        pauseModal.isVisible = false

        val mainTable = Table()

        val topRowTable = Table()
        val topRowLeftTable = Table()
        val topRowCenterTable = Table()
        val topRowRightTable = Table()
        topRowTable.add(topRowLeftTable).width(300f)
        topRowTable.add(topRowCenterTable).expandX()
        topRowTable.add(topRowRightTable).width(300f)
        val midRowTable = Table()
        val elementCounterWidget = ElementCounterWidget(game)

        hexMapWidget = HexMapWidget(game)
        hexMapWidget!!.soundVolume = PreferencesProvider.getPreferences().soundVolume
        hexMapWidget!!.cellActionListener = object : HexMapWidget.CellActionListener {
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
        midRowTable.add<HexMapWidget>(hexMapWidget).center().expand()

        mainTable.add(topRowTable).growX().row()
        mainTable.add(Label(levelData.name, StyleFactory.generateStandardLabelStyle(game))).row()
        mainTable.add(midRowTable).growX().growY().row()
        mainTable.add(elementCounterWidget).growX()

        val smallPauseButton = TextButton(" ", StyleFactory.generatePauseButtonStyle(game))
        smallPauseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                pauseModal.isVisible = !pauseModal.isVisible
            }
        })
        topRowLeftTable.left().top()
        topRowLeftTable.add(smallPauseButton).size(120f).pad(DEFAULT_PADDING_HALF).center()

        scoreLabel = Label("", StyleFactory.generateStandardLabelStyle(game))
        scoreLabel!!.setFontScale(SCORE_LABEL_FONT_SCALE)
        topRowCenterTable.add<Label>(scoreLabel)

        //Stack topRowRightStack = new Stack();
        manaLabel = TextButton("", StyleFactory.generateManaButtonStyle(game))
        topRowRightTable.add<TextButton>(manaLabel).size(150f).pad(DEFAULT_PADDING_HALF)

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

        gameData.gameDataChangedListener = object : GameData.GameDataChangedListener {
            override fun manaChanged(newValue: Int) {
                manaLabel!!.setText(newValue.toString())
            }

            override fun scoreChanged(newValue: Int) {
                scoreLabel!!.setText(newValue.toString())
            }

            override fun cellMapChanged(newMap: Array<Array<Cell?>>) {
                hexMapWidget!!.setCellArray(newMap)
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
                handleGameLostOrWon(payload)
            }
        }

        stage.addActor(
            ScreenContainer(
                LevelBackground(game, levelCode),
                SafeAreaContainer(mainTable),

                winLossModal,
                pauseModal
            )
        )
    }

    override fun onBackPressed(): Boolean {
        pauseModal.isVisible = !pauseModal.isVisible
        return true
    }

    private fun handleGameLostOrWon(payload: GameData.WinLossPayload) {
        winLossModal.setData(payload)
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

    private fun handleNextPressed() {
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
                bundle.putItem(SHOULD_RESTORE_SCROLL_POSITION_KEY, "true")
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
                bundle.putItem(SHOULD_RESTORE_SCROLL_POSITION_KEY, "true")
                game.gameStateManager.setState(
                    GameStateManager.GameState.LEVEL_SELECT,
                    bundle
                )
            }
        }
    }

    private fun handleRetryPressed() {
        winLossModal.isVisible = false

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
