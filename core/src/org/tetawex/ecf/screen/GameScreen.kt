package org.tetawex.ecf.screen

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
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.actor.HexMapActor
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.*
import org.tetawex.ecf.model.Cell
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider
import java.util.*

/**
 * ...
 */
class GameScreen(game: ECFGame, bundle: Bundle?) : BaseECFScreen(game) {

    private val levelData: LevelData

    private val starRegion: TextureRegion
    private val starDisabledRegion: TextureRegion
    private val winSound: Sound
    private val lossSound: Sound

    private val gameStage: Stage
    private var hexMapActor: HexMapActor? = null
    private var scoreLabel: Label? = null
    private var manaLabel: TextButton? = null

    private var fireCounterLabel: Label? = null
    private var waterCounterLabel: Label? = null

    private var airCounterLabel: Label? = null
    private var earthCounterLabel: Label? = null

    private var shadowCounterLabel: Label? = null
    private var lightCounterLabel: Label? = null

    private val gameData: GameData
    private var wlLabel: Label? = null
    private var wlScore: Label? = null
    private var wlSpareMana: Label? = null
    private var wlTotal: Label? = null
    private var wlReasonLabel: Label? = null
    private var winLossMenuButtonNext: TextButton? = null

    lateinit var pauseTable: Table
    lateinit var backgroundPause: Image

    private val preferences: ECFPreferences

    private val lcToStringMap: MutableMap<GameData.LossCondition, String>
    private var stars: Array<Image?> = arrayOf()
    private val levelCode: String

    init {
        winSound = game.assetManager.get("sounds/win.ogg", Sound::class.java)
        lossSound = game.assetManager.get("sounds/loss.ogg", Sound::class.java)

        starRegion = game.getTextureRegionFromAtlas("star")
        starDisabledRegion = game.getTextureRegionFromAtlas("star_ungained")

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

        preferences = PreferencesProvider.getPreferences()

        lcToStringMap = HashMap()
        lcToStringMap[GameData.LossCondition.NO_MANA] = "lc_no_mana"

        lcToStringMap[GameData.LossCondition.NO_FIRE] = "lc_no_fire"
        lcToStringMap[GameData.LossCondition.NO_WATER] = "lc_no_water"

        lcToStringMap[GameData.LossCondition.NO_AIR] = "lc_no_air"
        lcToStringMap[GameData.LossCondition.NO_EARTH] = "lc_no_earth"

        lcToStringMap[GameData.LossCondition.NO_SHADOW] = "lc_no_shadow"
        lcToStringMap[GameData.LossCondition.NO_LIGHT] = "lc_no_light"
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

        val background = Image(game.assetManager
                .get("backgrounds/" + levelCode + "background.png", Texture::class.java))
        background.setFillParent(true)
        backgroundPause = Image(game.assetManager
                .get("backgrounds/background_pause.png", Texture::class.java))
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
        val bottomRowTable = Table()

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
        mainTable.add(bottomRowTable).growX()

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
        val wlTable = Table()

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

            override fun elementsCountChanged(fire: Int, water: Int, air: Int, earth: Int, shadow: Int, light: Int, time: Int) {
                fireCounterLabel!!.setText(fire.toString() + "")
                waterCounterLabel!!.setText(water.toString() + "")
                airCounterLabel!!.setText(air.toString() + "")
                earthCounterLabel!!.setText(earth.toString() + "")
                shadowCounterLabel!!.setText(shadow.toString() + "")
                lightCounterLabel!!.setText(light.toString() + "")
            }

            override fun gameLostOrWon(won: Boolean, lossCondition: GameData.LossCondition?) {
                val totalScore = (gameData.getScore() + gameData.getMana() * 100).toInt()
                var frequency = 5
                backgroundPause.isVisible = true
                winLossMenuButtonNext!!.isVisible = won
                wlTable.isVisible = true
                wlScore!!.setText(" " + gameData.getScore())
                wlSpareMana!!.setText(" " + (gameData.getMana() * 100).toInt())
                wlTotal!!.setText(" $totalScore")
                if (won) {
                    winSound.play(preferences.soundVolume)

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
                    winLossMenuButtonNext!!.height = PAUSE_BUTTON_HEIGHT
                    wlReasonLabel!!.isVisible = false
                    wlLabel!!.setText(game.getLocalisedString("level_success"))

                    var starsCount = 1
                    if (totalScore > levelData.maxScore / 2)
                        starsCount = 2
                    if (totalScore >= levelData.maxScore)
                        starsCount = 3

                    for (i in 0 until starsCount) {
                        stars[i]!!.drawable = TextureRegionDrawable(starRegion)
                    }
                    if ("editor" != levelData.levelCode && "random" != levelData.levelCode) {
                        preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber].completed = true
                        if (preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber].stars < starsCount)
                            preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber].stars = starsCount
                        if (levelData.levelNumber + 1 < PreferencesProvider.getLevelCountForCode(levelCode))
                            preferences.getLevelCompletionStateList(levelCode)!![levelData.levelNumber + 1].unlocked = true
                    }
                    PreferencesProvider.flushPreferences()
                } else {
                    frequency = 10
                    lossSound.play(preferences.soundVolume)
                    winLossMenuButtonNext!!.height = 0f
                    wlLabel!!.setText(game.getLocalisedString("level_fail"))
                    wlReasonLabel!!.setText(game.getLocalisedString(lcToStringMap[lossCondition]!!))
                    wlReasonLabel!!.isVisible = true
                }
            }
        }
        //Element counter
        val elementLabelStyle = StyleFactory.generateDarkestLabelStyle(game)
        fireCounterLabel = Label("0", elementLabelStyle)
        waterCounterLabel = Label("0", elementLabelStyle)
        airCounterLabel = Label("0", elementLabelStyle)
        earthCounterLabel = Label("0", elementLabelStyle)
        shadowCounterLabel = Label("0", elementLabelStyle)
        lightCounterLabel = Label("0", elementLabelStyle)

        //fire-water
        val fwTable = Table()
        val fireTable = Table()
        val waterTable = Table()

        fireTable.add(Image(
                game.getTextureRegionFromAtlas("element_fire"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        fireTable.add<Label>(fireCounterLabel)
        waterTable.add(Image(
                game.getTextureRegionFromAtlas("element_water"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        waterTable.add<Label>(waterCounterLabel)

        fwTable.add(fireTable)
        fwTable.add(waterTable)

        //air-earth
        val aeTable = Table()
        val airTable = Table()
        val earthTable = Table()

        airTable.add(Image(
                game.getTextureRegionFromAtlas("element_air"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        airTable.add<Label>(airCounterLabel)
        earthTable.add(Image(
                game.getTextureRegionFromAtlas("element_earth"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        earthTable.add<Label>(earthCounterLabel)

        aeTable.add(airTable)
        aeTable.add(earthTable)

        //shadow-light
        val slTable = Table()
        val shadowTable = Table()
        val lightTable = Table()

        shadowTable.add(Image(
                game.getTextureRegionFromAtlas("element_shadow"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        shadowTable.add<Label>(shadowCounterLabel)
        lightTable.add(Image(
                game.getTextureRegionFromAtlas("element_light"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row()
        lightTable.add<Label>(lightCounterLabel)

        slTable.add(shadowTable)
        slTable.add(lightTable)

        bottomRowTable.add(fwTable).pad(40f)
        bottomRowTable.add(aeTable).prefWidth(1500f)
        bottomRowTable.add(slTable).pad(40f)


        //win/loss ui
        wlTable.isVisible = false
        wlLabel = Label("", StyleFactory.generateLargeStandardLabelStyle(game))
        wlReasonLabel = Label("", StyleFactory.generateStandardLabelStyle(game))
        wlReasonLabel!!.setWrap(true)
        wlReasonLabel!!.setAlignment(Align.center)
        wlTable.add<Label>(wlLabel).pad(20f).row()
        wlTable.add<Label>(wlReasonLabel).pad(20f).width(PAUSE_BUTTON_WIDTH).row()

        val starsTable = Table()
        stars = Array(3) { null }

        for (i in stars.indices) {
            val image = Image(starDisabledRegion)
            stars[i] = image
            starsTable.add(image).size(200f).pad(20f)
        }

        val scoreTable = Table()
        scoreTable.add(Label(game.getLocalisedString("score"), StyleFactory.generateStandardLabelStyle(game)))
        wlScore = Label("", StyleFactory.generateStandardLabelStyle(game))
        scoreTable.add<Label>(wlScore).padRight(40f)
        wlTable.add(scoreTable).pad(20f).row()

        val spareTable = Table()
        spareTable.add(Label(game.getLocalisedString("spare_mana"), StyleFactory.generateStandardLabelStyle(game)))
        wlSpareMana = Label("", StyleFactory.generateStandardLabelStyle(game))
        spareTable.add<Label>(wlSpareMana)
        wlTable.add(spareTable).pad(20f).row()

        val totalTable = Table()
        totalTable.add(Label(game.getLocalisedString("total"), StyleFactory.generateStandardLabelStyle(game)))
        wlTotal = Label("", StyleFactory.generateStandardLabelStyle(game))
        totalTable.add<Label>(wlTotal)
        wlTable.add(totalTable).pad(20f).row()
        wlTable.add(starsTable).pad(20f).row()

        stack.add(wlTable)

        winLossMenuButtonNext = TextButton(game.getLocalisedString("next"), StyleFactory.generateStandardMenuButtonStyle(game))
        winLossMenuButtonNext!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (levelCode == "editor") {
                    goBackToEditor()
                    return
                }
                else if (levelCode == "random") {
                    val bundle = Bundle()
                    val factory = RandomLevelFactory(Random().nextInt(10000), Random().nextInt(2) + 3, Random().nextInt(3) + 3)
                    bundle.putItem("levelData", LevelData(factory.theBoard, factory.mana, 10000, -1, "Random Level", "random"))
                    game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
                    return
                }
                else if (levelData.levelNumber + 1 >= PreferencesProvider.getLevelCountForCode(levelCode)) {
                    val bundle = Bundle()
                    bundle.putItem("levelCode", levelCode)
                    game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
                } else {
                    val bundle = Bundle()
                    bundle.putItem("levelCode", levelCode)
                    bundle.putItem("levelData",
                            LevelFactory.generateLevel(levelData.levelNumber + 1, levelCode))
                    game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
                }
            }
        })
        wlTable.add<TextButton>(winLossMenuButtonNext)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()

        val winLossMenuButtonRetry = TextButton(game.getLocalisedString("retry"), StyleFactory.generateStandardMenuButtonStyle(game))
        winLossMenuButtonRetry.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                wlTable.isVisible = false
                backgroundPause.isVisible = false
                gameData.cellArray = levelData.cellArray
                gameData.setScore(0)
                gameData.setMana(levelData.mana)
            }
        })
        wlTable.add(winLossMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()


        val winLossMenuButtonQuit = TextButton(game.getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(game))
        winLossMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if ("editor" == levelCode) {
                    goBackToEditor()
                    return
                }
                if (levelCode == "random") {
                    game.gameStateManager.setState(GameStateManager.GameState.HIGHSCORES, null)
                    return
                }
                val bundle = Bundle()
                bundle.putItem("levelCode", levelCode)
                game.gameStateManager.setState(GameStateManager.GameState.LEVEL_SELECT, bundle)
            }
        })
        wlTable.add(winLossMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()

        //pause ui
        pauseTable.center()

        val pauseMenuButtonContinue = TextButton(game.getLocalisedString("continue"), StyleFactory.generateStandardMenuButtonStyle(game))
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

        val pauseMenuButtonRetry = TextButton(game.getLocalisedString("retry"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonRetry.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
                gameData.cellArray = levelData.cellArray
                gameData.setScore(0)
                gameData.setMana(levelData.mana)
            }
        })
        pauseMenuButtonRetry.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()


        val pauseMenuButtonQuit = TextButton(game.getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(game))
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

    override fun onBackPressed() {
        if (pauseTable.isVisible) {
            pauseTable.isVisible = false
            backgroundPause.isVisible = false
        } else {
            pauseTable.isVisible = true
            backgroundPause.isVisible = true
        }
    }

    private fun goBackToEditor() {
        val bundle = Bundle()
        bundle.putItem("levelData", levelData)
        game.gameStateManager.setState(GameStateManager.GameState.EDITOR, bundle)
    }

    companion object {
        private val PAUSE_BUTTON_WIDTH = 1275f
        private val PAUSE_BUTTON_HEIGHT = 252f
        private val PAUSE_BUTTON_PAD = 32f
        private val PAUSE_BUTTON_FONT_SCALE = 1f
        private val MANA_LABEL_FONT_SCALE = 1f
        private val SCORE_LABEL_FONT_SCALE = 1f
        private val ELEMENT_COUNTER_IMAGE_SIZE = 130f
    }
}
