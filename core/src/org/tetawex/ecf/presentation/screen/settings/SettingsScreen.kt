package org.tetawex.ecf.presentation.screen.settings

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.presentation.screen.BaseScreen
import org.tetawex.ecf.presentation.widget.SafeAreaContainer
import org.tetawex.ecf.presentation.widget.ScreenContainer
import org.tetawex.ecf.presentation.widget.background.CommonBackground
import org.tetawex.ecf.presentation.widget.background.LevelBackground
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.FontCharacters
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class SettingsScreen(game: ECFGame, bundle: Bundle?) : BaseScreen(game) {
    private val preferences: ECFPreferences = PreferencesProvider.getPreferences()

    private val musicSlider: Slider
    private val soundSlider: Slider

    private val languageSelectModal: LanguageSelectModal

    init {
        val mainTable = Table()

        //Sound table
        val soundTable = Table()
        soundSlider = Slider(
            0f, 1f, 0.0001f, false,
            StyleFactory.generateStandardSliderStyle(game)
        )
        soundSlider.value = preferences.soundVolume
        soundTable.add(
            Label(
                game.getLocalisedString("sound"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        ).width(250f).padRight(DEFAULT_PADDING_HALF)
        soundTable.add(Image(game.getTextureRegionFromAtlas("sound_off"))).size(86f, 86f)
        soundTable.add<Slider>(soundSlider).growX().pad(DEFAULT_PADDING_HALF)
        soundTable.add(Image(game.getTextureRegionFromAtlas("sound_on"))).size(86f, 86f)
        soundTable.padRight(DEFAULT_PADDING_HALF)
        soundTable.padLeft(DEFAULT_PADDING_HALF)
        //Music table
        val musicTable = Table()
        musicSlider = Slider(
            0f, 1f, 0.0001f, false,
            StyleFactory.generateStandardSliderStyle(game)
        )
        musicSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.setMusicVolume(musicSlider.value)
            }
        })
        musicSlider.value = preferences.musicVolume
        musicTable.add(
            Label(
                game.getLocalisedString("music"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        ).width(250f).padRight(DEFAULT_PADDING_HALF)
        musicTable.add(Image(game.getTextureRegionFromAtlas("music_off"))).size(86f, 86f)
        musicTable.add<Slider>(musicSlider).growX().pad(DEFAULT_PADDING_HALF)
        musicTable.add(Image(game.getTextureRegionFromAtlas("music_on"))).size(86f, 86f)
        musicTable.padRight(DEFAULT_PADDING_HALF)
        musicTable.padLeft(DEFAULT_PADDING_HALF)

        //Back button
        val menuButtonBackToMainMenu = TextButton(
            game.getLocalisedString("back"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        menuButtonBackToMainMenu.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onBackPressed()
            }
        })
        menuButtonBackToMainMenu.label.setFontScale(BUTTON_FONT_SCALE)

        //Modal
        languageSelectModal = LanguageSelectModal(
            game = game,
            onBackPressed = {
                handleLanguageModalBackPress()
            },
            onLanguageSelected = { language ->
                preferences.selectedLanguage = language.code
                PreferencesProvider.flushPreferences()
                game.setupLocalisation()
                game.gameStateManager.setState(GameStateManager.GameState.SETTINGS, null)
            },
            onDefaultLanguageSelected = {
                preferences.selectedLanguageCode = "default"
                preferences.selectedCountryCode = ""
                PreferencesProvider.flushPreferences()
                game.setupLocalisation()
                game.gameStateManager.setState(GameStateManager.GameState.SETTINGS, null)
            }
        )
        languageSelectModal.isVisible = false

        //Language table
        val selectedLanguageLabel = Label(
            FontCharacters.codeToLanguageMap[preferences.selectedLanguage],
            StyleFactory.generateDarkerLabelStyle(game)
        )
        val languageTable = Table()
        languageTable.add(
            Label(
                game.getLocalisedString("language"),
                StyleFactory.generateStandardLabelStyle(game)
            )
        )
        val secondaryLanguageTable = Table()
        secondaryLanguageTable.add<Label>(selectedLanguageLabel).pad(DEFAULT_PADDING_HALF)
        secondaryLanguageTable.add(Image(game.getTextureRegionFromAtlas("arrow"))).size(35f, 57f)
        languageTable.add(secondaryLanguageTable).growX()
        secondaryLanguageTable.right()
        languageTable.padRight(DEFAULT_PADDING_HALF)
        languageTable.padLeft(DEFAULT_PADDING_HALF)

        languageTable.touchable = Touchable.enabled
        languageTable.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                languageSelectModal.isVisible = true
            }
        })


        //Dumping everything into the main table
        mainTable.add(soundTable).growX().row()
        mainTable.add(musicTable).growX().row()
        mainTable.add(languageTable).growX().row()
        mainTable.add(menuButtonBackToMainMenu)
            .size(BUTTON_WIDTH, BUTTON_HEIGHT)
            .center().pad(BUTTON_PAD).row()

        stage.addActor(
            ScreenContainer(
                LevelBackground(game),
                SafeAreaContainer(mainTable),
                languageSelectModal
            )
        )
    }

    private fun handleLanguageModalBackPress() {
        languageSelectModal.isVisible = false
    }

    override fun onBackPressed(): Boolean {
        if (languageSelectModal.isVisible) {
            languageSelectModal.isVisible = false
            return true
        }
        game.gameStateManager.setState(GameStateManager.GameState.MAIN_MENU, null)
        return true
    }

    override fun dispose() {
        preferences.musicVolume = musicSlider.value
        preferences.soundVolume = soundSlider.value
        super.dispose()
    }
}
