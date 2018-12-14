package org.tetawex.ecf.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.Language
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.FontCharacters
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class SettingsScreen(game: ECFGame, bundle: Bundle?) : BaseScreen<ECFGame>(game) {

    private val preferences: ECFPreferences

    private val stage: Stage
    private var musicSlider: Slider? = null
    private var soundSlider: Slider? = null
    private var selectedLanguageLabel: Label? = null

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

        //Sound table
        val soundTable = Table()
        soundSlider = Slider(
                0f, 1f, 0.0001f, false,
                StyleFactory.generateStandardSliderStyle(game))
        soundSlider!!.value = preferences.soundVolume
        soundTable.add(Label(game.getLocalisedString("sound"),
                StyleFactory.generateStandardLabelStyle(game))).width(250f).padRight(40f)
        soundTable.add(Image(game.getTextureRegionFromAtlas("sound_off"))).size(86f, 86f)
        soundTable.add<Slider>(soundSlider).growX().pad(40f)
        soundTable.add(Image(game.getTextureRegionFromAtlas("sound_on"))).size(86f, 86f)
        soundTable.padRight(80f)
        soundTable.padLeft(80f)
        //Music table
        val musicTable = Table()
        musicSlider = Slider(
                0f, 1f, 0.0001f, false,
                StyleFactory.generateStandardSliderStyle(game))
        musicSlider!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.setMusicVolume(musicSlider!!.value)
            }
        })
        musicSlider!!.value = preferences.musicVolume
        musicTable.add(Label(game.getLocalisedString("music"), StyleFactory.generateStandardLabelStyle(game))).width(250f).padRight(40f)
        musicTable.add(Image(game.getTextureRegionFromAtlas("music_off"))).size(86f, 86f)
        musicTable.add<Slider>(musicSlider).growX().pad(40f)
        musicTable.add(Image(game.getTextureRegionFromAtlas("music_on"))).size(86f, 86f)
        musicTable.padRight(80f)
        musicTable.padLeft(80f)
        //Language table
        selectedLanguageLabel = Label(FontCharacters.codeToLanguageMap[preferences.selectedLanguage],
                StyleFactory.generateDarkerLabelStyle(game))
        val languageTable = Table()
        languageTable.add(Label(game.getLocalisedString("language"), StyleFactory.generateStandardLabelStyle(game)))
        val secondaryLanguageTable = Table()
        secondaryLanguageTable.add<Label>(selectedLanguageLabel).pad(40f)
        secondaryLanguageTable.add(Image(game.getTextureRegionFromAtlas("arrow"))).size(35f, 57f)
        languageTable.add(secondaryLanguageTable).growX()
        secondaryLanguageTable.right()
        languageTable.padRight(80f)
        languageTable.padLeft(80f)
        //Back button
        val menuButtonBackToMainMenu = TextButton(game.getLocalisedString("back"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        menuButtonBackToMainMenu.label.setFontScale(BUTTON_FONT_SCALE)
        //Dumping everything into the main table
        mainTable.add(soundTable).growX().row()
        mainTable.add(musicTable).growX().row()
        mainTable.add(languageTable).growX().row()
        mainTable.add(menuButtonBackToMainMenu)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()
        //Initializing language list
        val langStack = Stack()
        langStack.setFillParent(true)
        langStack.isVisible = false
        langStack.add(Image(
                game.assetManager
                        .get("backgrounds/background.png",
                                Texture::class.java)))
        val languageSelectTable = Table()
        langStack.add(languageSelectTable)
        stack.add(langStack)
        val languageListTable = Table()
        languageSelectTable.add(languageListTable).pad(80f).row()
        val languageSelectBackButton = TextButton(game.getLocalisedString("back"),
                StyleFactory.generateStandardMenuButtonStyle(game))
        languageSelectBackButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                langStack.isVisible = false
            }
        })
        languageSelectTable.add(languageSelectBackButton)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()

        val defaultButton = TextButton(game.getLocalisedString("auto"), StyleFactory.generateLanguageMenuButtonStyle(game))
        defaultButton.left()
        languageListTable.add(defaultButton).size(BUTTON_WIDTH, BUTTON_HEIGHT_HALVED)
                .center().pad(BUTTON_PAD).row()
        defaultButton.label.setAlignment(Align.left)
        defaultButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                preferences.selectedLanguageCode = "default"
                preferences.selectedCountryCode = ""
                PreferencesProvider.flushPreferences()
                game.setupLocalisation()
                game.gameStateManager.setState(GameStateManager.GameState.SETTINGS, null)
            }
        })
        for (language in FontCharacters.supportedLanguages) {
            val button = TextButton(language.name, StyleFactory.generateLanguageMenuButtonStyle(game))
            button.label.setAlignment(Align.left)
            languageListTable.add(button)
                    .size(BUTTON_WIDTH, BUTTON_HEIGHT_HALVED)
                    .center().pad(BUTTON_PAD).row()
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    preferences.selectedLanguage = language.code!!
                    PreferencesProvider.flushPreferences()
                    game.setupLocalisation()
                    game.gameStateManager.setState(GameStateManager.GameState.SETTINGS, null)
                }
            })
        }
        languageTable.touchable = Touchable.enabled
        languageTable.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                langStack.isVisible = true
            }
        })
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        stage.viewport.camera.update()
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun onBackPressed() {
        game.gameStateManager.setState(GameStateManager.GameState.MAIN_MENU, null)
    }

    override fun dispose() {
        preferences.musicVolume = musicSlider!!.value
        preferences.soundVolume = soundSlider!!.value
    }

    companion object {
        private val BUTTON_WIDTH = 1275f
        private val BUTTON_HEIGHT = 255f
        private val BUTTON_HEIGHT_HALVED = 120f
        private val BUTTON_PAD = 40f
        private val BUTTON_FONT_SCALE = 1f
        private val LABEL_FONT_SCALE = 1f
    }
}
