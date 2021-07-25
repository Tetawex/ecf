package org.tetawex.ecf.core

import org.tetawex.ecf.presentation.screen.*
import org.tetawex.ecf.tutorial.MotTutorialScreen
import org.tetawex.ecf.tutorial.TutorialScreen
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
        LOADING, MAIN_MENU, GAME, SETTINGS, HIGHSCORES, MODE_SELECT, LEVEL_SELECT, TUTORIAL, EDITOR, MOT_TUTORIAL, LEVEL_PACK_SELECT
    }

    init {
//        setState(GameState.LOADING, null)
    }

    constructor(game: ECFGame, state: GameState) : this(game) {
        setState(state, null)
    }

    fun renderCurrentScreen(deltaTime: Float) {
        currentScreen?.render(deltaTime)
    }

    fun resize(width: Int, height: Int) {
        currentScreen?.resize(width, height)
    }

    fun setState(gameState: GameState, bundle: Bundle?) {
        currentState = gameState
        currentScreen?.dispose()

        when (currentState) {
            GameState.TUTORIAL -> currentScreen = TutorialScreen(game, bundle)
            GameState.MOT_TUTORIAL -> currentScreen = MotTutorialScreen(game, bundle)
            GameState.MODE_SELECT -> currentScreen = PlayModeSelectScreen(game, bundle)
            GameState.LEVEL_SELECT -> currentScreen = LevelSelectScreen(game, bundle)
            GameState.HIGHSCORES -> currentScreen = HighscoresScreen(game, bundle)
            GameState.MAIN_MENU -> currentScreen = MainMenuScreen(game, bundle)
            GameState.SETTINGS -> currentScreen = SettingsScreen(game, bundle)
            GameState.GAME -> currentScreen = GameScreen(game, bundle)
            GameState.LEVEL_PACK_SELECT -> currentScreen = LevelPackSelectScreen(game, bundle)
            GameState.EDITOR -> currentScreen = EditorScreen(game, bundle)
            GameState.LOADING -> currentScreen = LoadingScreen(game, bundle)
        }
    }
}
