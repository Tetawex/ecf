package org.tetawex.ecf.presentation.screen.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import org.tetawex.ecf.core.ECFGame
import org.tetawex.ecf.presentation.MODAL_BUTTON_PADDING
import org.tetawex.ecf.presentation.PAUSE_BUTTON_FONT_SCALE
import org.tetawex.ecf.presentation.PAUSE_BUTTON_HEIGHT
import org.tetawex.ecf.presentation.PAUSE_BUTTON_WIDTH
import org.tetawex.ecf.presentation.screen.StyleFactory

class PauseModal(
    val game: ECFGame,
    val onContinuePressed: () -> Unit,
    val onRetryPressed: () -> Unit,
    val onQuitPressed: () -> Unit
) : Table() {

    init {
        //pause ui
        center()

        val pauseMenuButtonContinue = TextButton(
            game.getLocalisedString("continue"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonContinue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onContinuePressed()
            }
        })
        pauseMenuButtonContinue.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        add(pauseMenuButtonContinue)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(MODAL_BUTTON_PADDING).row()

        val pauseMenuButtonRetry = TextButton(
            game.getLocalisedString("retry"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonRetry.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onRetryPressed()
            }
        })
        pauseMenuButtonRetry.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        add(pauseMenuButtonRetry)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(MODAL_BUTTON_PADDING).row()


        val pauseMenuButtonQuit = TextButton(
            game.getLocalisedString("quit"),
            StyleFactory.generateStandardMenuButtonStyle(game)
        )
        pauseMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onQuitPressed()
            }
        })
        pauseMenuButtonQuit.label.setFontScale(PAUSE_BUTTON_FONT_SCALE)
        add(pauseMenuButtonQuit)
            .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
            .center().pad(MODAL_BUTTON_PADDING).row()
    }
}