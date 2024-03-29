package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.core.GameStateManager
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.presentation.*
import org.tetawex.ecf.presentation.widget.background.CommonBackground
import org.tetawex.ecf.util.Bundle
import org.tetawex.ecf.util.PreferencesProvider

/**
 * Created by Tetawex on 03.06.2017.
 */
class MainMenuScreen(game: ECFGame, bundle: Bundle?) : BaseScreen(game) {
    private val preferences: ECFPreferences

    init {
        preferences = PreferencesProvider.getPreferences()
        initUi()
    }

    private fun initUi() {
        val stack = Stack()
        stack.setFillParent(true)
        stack.add(CommonBackground(game))
        val titleTable = Table()

        val titleTexture = game.assetManager
                .get("backgrounds/text_logo.png",
                        Texture::class.java)
        titleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        titleTable.add<Image>(Image(titleTexture)).size(1270f, 134f).padTop(300f)
        titleTable.top()

        val mainTable = Table()
        mainTable.setFillParent(true)
        stack.add(mainTable)
        stack.add(titleTable)
        stage.addActor(stack)
        val menuButtonPlay = TextButton(game.getLocalisedString("play"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonPlay.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (preferences.completedTutorial)
                    game.gameStateManager.setState(GameStateManager.GameState.MODE_SELECT, null)
                else
                    game.gameStateManager.setState(GameStateManager.GameState.TUTORIAL, null)
            }
        })
        menuButtonPlay.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonPlay)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()

        val menuButtonHighscores = TextButton(game.getLocalisedString("highscores"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonHighscores.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.HIGHSCORES, null)
            }
        })
        menuButtonHighscores.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonHighscores)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()

        val menuButtonSettings = TextButton(game.getLocalisedString("settings"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonSettings.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                game.gameStateManager.setState(GameStateManager.GameState.SETTINGS, null)
            }
        })
        menuButtonSettings.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonSettings)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()

        val menuButtonQuit = TextButton(game.getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(game))
        menuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                Gdx.app.exit()
            }
        })
        menuButtonQuit.label.setFontScale(BUTTON_FONT_SCALE)
        mainTable.add(menuButtonQuit)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row()
    }
}
