package org.tetawex.ecf.core

import org.tetawex.ecf.screen.*
import org.tetawex.ecf.tutorial.*
import org.tetawex.ecf.util.Bundle

/**
 * Created by Tetawex on 30.12.2016.
 */
class GameStateManager(private val game: ECFGame) {

    var currentScreen: BaseScreen<ECFGame>? = null
        private set
    private var currentState: GameState? = null
    fun dispose() {
        currentScreen!!.dispose()
    }

    enum class GameState {
        MAIN_MENU, GAME, SETTINGS, HIGHSCORES, MODE_SELECT, LEVEL_SELECT, TUTORIAL, EDITOR, MOT_TUTORIAL, LEVEL_PACK_SELECT
    }

    init {
        setState(GameState.MAIN_MENU, null)
    }

    constructor(game: ECFGame, state: GameState) : this(game) {
        setState(state, null)
    }

    fun renderCurrentScreen(deltaTime: Float) {
        currentScreen!!.render(deltaTime)
    }

    fun setState(gameState: GameState, bundle: Bundle?) {
        currentState = gameState
        if (currentScreen != null)
            currentScreen!!.dispose()

        when (currentState) {
            GameStateManager.GameState.TUTORIAL -> currentScreen = org.tetawex.ecf.tutorial.TutorialScreen(game, bundle)
            GameStateManager.GameState.MOT_TUTORIAL -> currentScreen = MotTutorialScreen(game, bundle)
            GameStateManager.GameState.MODE_SELECT -> currentScreen = PlayModeSelectScreen(game, bundle)
            GameStateManager.GameState.LEVEL_SELECT -> currentScreen = LevelSelectScreen(game, bundle)
            GameStateManager.GameState.HIGHSCORES -> currentScreen = HighscoresScreen(game, bundle)
            GameStateManager.GameState.MAIN_MENU -> currentScreen = MainMenuScreen(game, bundle)
            GameStateManager.GameState.SETTINGS -> currentScreen = SettingsScreen(game, bundle)
            GameStateManager.GameState.GAME -> currentScreen = GameScreen(game, bundle)
            GameStateManager.GameState.LEVEL_PACK_SELECT -> currentScreen = LevelPackSelectScreen(game, bundle)
            GameStateManager.GameState.EDITOR -> currentScreen = EditorScreen(game, bundle)
        }
    }
}
