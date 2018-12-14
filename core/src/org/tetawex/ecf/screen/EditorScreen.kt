package org.tetawex.ecf.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.actor.EditorHexMapActor
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.*
import org.tetawex.ecf.util.*

/**
 * ...
 */
class EditorScreen(game: ECFGame, bundle: Bundle?) : BaseECFScreen(game) {

    private val gameStage: Stage
    private var hexMapActor: EditorHexMapActor? = null

    private var nameField: TextField? = null
    private var dimXField: TextField? = null
    private var dimYField: TextField? = null
    private var maxScoreField: TextField? = null
    private var manaField: TextField? = null

    lateinit var pauseTable: Table
    lateinit var midRowTable: Table

    lateinit var backgroundPause: Image

    private val preferences: ECFPreferences

    enum class ButtonAction {
        ADD_FIRE, ADD_WATER, ADD_AIR, ADD_EARTH, ADD_SHADOW, ADD_LIGHT, ADD_TIME,
        REMOVE_CELL, PLAY_LEVEL, SAVE_LEVEL, LOAD_LEVEL;


        companion object {

            val allActions: Array<ButtonAction>
                get() {
                    val array = Array<ButtonAction>()
                    array.addAll(*values())
                    return array
                }

            val topRowActions: Array<ButtonAction>
                get() {
                    val array = Array<ButtonAction>()
                    array.addAll(ADD_FIRE, ADD_WATER, ADD_AIR, ADD_EARTH, ADD_SHADOW, ADD_LIGHT, ADD_TIME,
                            REMOVE_CELL)
                    return array
                }

            val bottomRowActions: Array<ButtonAction>
                get() {
                    val array = Array<ButtonAction>()
                    array.addAll(PLAY_LEVEL, SAVE_LEVEL, LOAD_LEVEL)
                    return array
                }
        }
    }

    init {

        val camera = OrthographicCamera(1440f, 2560f)
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        gameStage = Stage(ExtendViewport(1440f, 2560f, camera))

        Gdx.input.inputProcessor = gameStage

        initUi()

        bundle?.getItem("levelData", LevelData::class.java)?.let { levelData ->
            resolveLoadedLevelData(levelData)
        }

        preferences = PreferencesProvider.getPreferences()
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
                .get("backgrounds/background.png", Texture::class.java))
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
        midRowTable = Table()
        val bottomRowTable = Table()
        mainTable.add(topRowTable).growX().row()

        hexMapActor = EditorHexMapActor(game)
        hexMapActor!!.soundVolume = PreferencesProvider.getPreferences().soundVolume
        hexMapActor!!.setCellArray(CellArrayFactory.generateEmptyCellArray(2, 2))
        midRowTable.add<EditorHexMapActor>(hexMapActor).center().expand()

        mainTable.setFillParent(true)
        mainTable.add(midRowTable).growX().growY().row()
        mainTable.add(bottomRowTable).growX()

        val pauseButton = TextButton(" ", StyleFactory.generatePauseButtonStyle(game))
        pauseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        topRowLeftTable.left().top()
        topRowLeftTable.add(pauseButton).size(120f).pad(40f).center()


        //element + null buttons
        val elementTable = Table()
        for (action in ButtonAction.topRowActions) {
            elementTable.add(generateButtonForAction(action)).size(142f).pad(PAUSE_BUTTON_PAD / 4)
        }
        bottomRowTable.add(elementTable).pad(16f).growX().row()

        bottomRowTable.add(createLevelSpecsTable()).pad(PAUSE_BUTTON_PAD).grow().row()
        bottomRowTable.add(createLevelResizeTable()).pad(PAUSE_BUTTON_PAD).padBottom(32f).grow()

        //pause ui
        pauseTable.center()

        val pauseMenuButtonContinue = TextButton(game.getLocalisedString("continue"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonContinue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
            }
        })

        val pauseMenuButtonPlay = TextButton(game.getLocalisedString("play"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonPlay.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val bundle = Bundle()
                bundle.putItem("levelData", createLevelData())
                game.gameStateManager.setState(GameStateManager.GameState.GAME, bundle)
            }
        })
        pauseMenuButtonPlay.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonPlay)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()

        pauseMenuButtonContinue.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonContinue)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()
        //save
        val pauseMenuButtonSaveLevel = TextButton(game.getLocalisedString("save"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonSaveLevel.addListener(object : ChangeListener() {
            @Volatile
            var busy: Boolean = false

            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (!game.actionResolver.externalStorageAccessible())
                    return
                val thread = Thread(Runnable {
                    if (busy)
                        return@Runnable
                    busy = true
                    try {
                        val levelData: LevelData
                        levelData = createLevelData()

                        val handle = Gdx.files.external(EDITOR_LEVEL_STORAGE +
                                levelData.name + ".json")
                        handle.writeString(LevelDataUtils.toJson(levelData), false)
                    } catch (e: Exception) {
                    }

                    busy = false
                })
                thread.isDaemon = false
                thread.run()

            }
        })
        pauseMenuButtonSaveLevel.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonSaveLevel)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()
        //load
        val pauseMenuButtonLoadLevel = TextButton(game.getLocalisedString("load"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonLoadLevel.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (!game.actionResolver.externalStorageAccessible())
                    return
                pauseTable.isVisible = false
                backgroundPause.isVisible = false
                val files = object : FileChooser(
                        "",
                        StyleFactory.generateFileChooserStyle(game),
                        game) {
                    override fun result(`object`: Any?) {
                        if (`object` is FileHandle) {
                            try {
                                val file = getFile()

                                val levelData = LevelDataUtils.fromJson(file!!.readString())
                                resolveLoadedLevelData(levelData)
                            } catch (e: Exception) {
                            }

                        }

                    }
                }
                files.setDirectory(Gdx.files.external(EDITOR_LEVEL_STORAGE))
                files.show(gameStage)
            }
        })
        pauseMenuButtonLoadLevel.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        pauseTable.add(pauseMenuButtonLoadLevel)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row()


        val pauseMenuButtonQuit = TextButton(game.getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(game))
        pauseMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                pauseTable.isVisible = !pauseTable.isVisible
                backgroundPause.isVisible = !backgroundPause.isVisible
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

    fun generateButtonForAction(action: ButtonAction): TextButton {
        val button = TextButton("", StyleFactory.generateActionButtonStyle(game, action))

        when (action) {
            EditorScreen.ButtonAction.ADD_FIRE -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.FIRE)
                }
            })
            EditorScreen.ButtonAction.ADD_WATER -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.WATER)
                }
            })
            EditorScreen.ButtonAction.ADD_AIR -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.AIR)
                }
            })
            EditorScreen.ButtonAction.ADD_EARTH -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.EARTH)
                }
            })
            EditorScreen.ButtonAction.ADD_SHADOW -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.SHADOW)
                }
            })
            EditorScreen.ButtonAction.ADD_LIGHT -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.LIGHT)
                }
            })
            EditorScreen.ButtonAction.ADD_TIME -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.addElementToSelectedCell(Element.TIME)
                }
            })
            EditorScreen.ButtonAction.REMOVE_CELL -> button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    hexMapActor!!.removeOrCreateCell()
                }
            })
            EditorScreen.ButtonAction.SAVE_LEVEL -> {
            }
            EditorScreen.ButtonAction.LOAD_LEVEL -> {
            }
            EditorScreen.ButtonAction.PLAY_LEVEL -> {
            }
        }

        return button
    }

    private fun createLevelSpecsTable(): Table {
        val placeholder = Table()
        nameField = TextField("", StyleFactory.generateEditorTextFieldStyle(game))
        nameField!!.setAlignment(Align.center)
        nameField!!.messageText = game.getLocalisedString("hint_name")

        maxScoreField = TextField("1000", StyleFactory.generateEditorTextFieldStyle(game))
        maxScoreField!!.setAlignment(Align.center)
        maxScoreField!!.messageText = game.getLocalisedString("hint_max_score")

        manaField = TextField("1", StyleFactory.generateEditorTextFieldStyle(game))
        manaField!!.setAlignment(Align.center)
        manaField!!.messageText = game.getLocalisedString("hint_mana")

        placeholder.add<TextField>(nameField).growX().padRight(16f)
        placeholder.add<TextField>(maxScoreField).width(512f).padRight(16f)
        placeholder.add<TextField>(manaField).width(256f).row()
        return placeholder

    }

    private fun createLevelResizeTable(): Table {
        val placeholder = Table()

        dimXField = TextField("", StyleFactory.generateEditorTextFieldStyle(game))
        dimXField!!.setAlignment(Align.center)
        dimXField!!.text = "2"
        dimXField!!.messageText = game.getLocalisedString("hint_width")

        dimYField = TextField("", StyleFactory.generateEditorTextFieldStyle(game))
        dimYField!!.setAlignment(Align.center)
        dimYField!!.text = "2"
        dimYField!!.messageText = game.getLocalisedString("hint_height")

        val applyButton = TextButton(game.getLocalisedString("apply"),
                StyleFactory.generateStandardMenuButtonStyle(game))
        applyButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                try {
                    hexMapActor!!.setHexSize(IntVector2(
                            Integer.valueOf(dimXField!!.text),
                            Integer.valueOf(dimYField!!.text)))
                    midRowTable.reset()
                    midRowTable.add<EditorHexMapActor>(hexMapActor).center().expand()

                } catch (e: Exception) {
                    dimXField!!.text = hexMapActor!!.getCellArray().size.toString()
                    dimYField!!.text = hexMapActor!!.getCellArray()[0].size.toString()
                }

            }
        })

        placeholder.add<TextField>(dimXField).width(400f).padRight(16f)
        placeholder.add<TextField>(dimYField).width(400f).padRight(16f)
        placeholder.add(applyButton).growX()
        return placeholder

    }

    private fun createLevelData(): LevelData {
        val levelData = LevelData()

        levelData.cellArray = hexMapActor!!.getCellArray()

        levelData.levelCode = "editor"
        levelData.levelNumber = 0

        levelData.maxScore = Integer.valueOf(maxScoreField!!.text)
        levelData.mana = Integer.valueOf(manaField!!.text)

        levelData.name = nameField!!.text

        return levelData
    }

    private fun resolveLoadedLevelData(data: LevelData) {
        hexMapActor!!.setCellArray(data.cellArray)
        midRowTable.reset()
        midRowTable.add<EditorHexMapActor>(hexMapActor).center().expand()

        dimXField!!.text = hexMapActor!!.getCellArray().size.toString()
        dimYField!!.text = hexMapActor!!.getCellArray()[0].size.toString()

        manaField!!.text = data.mana.toString()
        maxScoreField!!.text = data.maxScore.toString()
        nameField!!.text = data.name.toString()
    }

    companion object {
        var EDITOR_LEVEL_STORAGE = "Elementality/Levels/"

        private val PAUSE_BUTTON_WIDTH = 1275f
        private val PAUSE_BUTTON_HEIGHT = 252f
        private val PAUSE_BUTTON_PAD = 32f
        private val PAUSE_BUTTON_FONT_SCALE = 1f
        private val MANA_LABEL_FONT_SCALE = 1f
        private val SCORE_LABEL_FONT_SCALE = 1f
        private val ELEMENT_COUNTER_IMAGE_SIZE = 130f
    }
}
