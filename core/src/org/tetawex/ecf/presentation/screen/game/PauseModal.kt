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
import org.tetawex.ecf.presentation.StyleFactory
import org.tetawex.ecf.presentation.widget.OutlineTextButton

class PauseModal(
    val game: ECFGame,
    val onContinuePressed: () -> Unit,
    val onRetryPressed: () -> Unit,
    val onQuitPressed: () -> Unit
) : Table() {

    init {
        center()

        val pauseMenuButtonContinue = OutlineTextButton(
            game.getLocalisedString("continue"), game
        )
        pauseMenuButtonContinue.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onContinuePressed()
            }
        })
        add(pauseMenuButtonContinue)
            .center()
            .pad(MODAL_BUTTON_PADDING)
            .row()

        val pauseMenuButtonRetry = OutlineTextButton(
            game.getLocalisedString("retry"), game
        )
        pauseMenuButtonRetry.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onRetryPressed()
            }
        })
        add(pauseMenuButtonRetry)
            .center()
            .pad(MODAL_BUTTON_PADDING)
            .row()


        val pauseMenuButtonQuit = OutlineTextButton(
            game.getLocalisedString("quit"), game
        )
        pauseMenuButtonQuit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                onQuitPressed()
            }
        })
        add(pauseMenuButtonQuit)
            .center()
            .pad(MODAL_BUTTON_PADDING)
            .row()
    }
}