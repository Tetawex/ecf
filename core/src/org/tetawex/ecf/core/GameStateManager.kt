package org.tetawex.ecf.core

import org.tetawex.ecf.presentation.screen.*
import org.tetawex.ecf.presentation.screen.game.GameScreen
import org.tetawex.ecf.presentation.screen.levelselect.LevelSelectScreen
import org.tetawex.ecf.presentation.screen.settings.SettingsScreen
import org.tetawex.ecf.presentation.tutorial.MotTutorialScreen
import org.tetawex.ecf.presentation.tutorial.TutorialScreen
import org.tetawex.ecf.util.Bundle

/**
 * Created by Tetawex on 30.12.2016.
 */
class GameStateManager(private val game: ECFGame) {
    var currentScreen: BaseScreen? = null
        private set
    private var currentState: GameState? = null

    fun dispose() {
        currentScreen!!.dispose()
    }

    enum class GameState {
        LOADING, MAIN_MENU, GAME, SETTINGS, HIGHSCORES, MODE_SELECT, LEVEL_SELECT, TUTORIAL, EDITOR, MOT_TUTORIAL, LEVEL_PACK_SELECT
    }

    init {
        setState(GameState.LOADING, null)
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

    private val savedStates: MutableMap<GameState, Bundle> = mutableMapOf()

    fun saveStateForGameState(gameState: GameState, bundle: Bundle) {
        savedStates[gameState] = bundle
    }

    fun setState(gameState: GameState, params: Bundle?) {
        currentState = gameState
        currentScreen?.dispose()

        when (currentState) {
            GameState.TUTORIAL -> currentScreen = TutorialScreen(game, params)
            GameState.MOT_TUTORIAL -> currentScreen = MotTutorialScreen(game, params)
            GameState.MODE_SELECT -> currentScreen = PlayModeSelectScreen(game, params)
            GameState.LEVEL_SELECT -> currentScreen =
                LevelSelectScreen(game, params, savedStates[GameState.LEVEL_SELECT])
            GameState.HIGHSCORES -> currentScreen = HighscoresScreen(game, params)
            GameState.MAIN_MENU -> currentScreen = MainMenuScreen(game, params)
            GameState.SETTINGS -> currentScreen = SettingsScreen(game, params)
            GameState.GAME -> currentScreen = GameScreen(game, params)
            GameState.LEVEL_PACK_SELECT -> currentScreen = LevelPackSelectScreen(game, params)
            GameState.EDITOR -> currentScreen = EditorScreen(game, params)
            GameState.LOADING -> currentScreen = LoadingScreen(game, params)
        }
    }
}
