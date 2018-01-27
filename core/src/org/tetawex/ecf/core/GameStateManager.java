package org.tetawex.ecf.core;

import com.badlogic.gdx.Screen;
import org.tetawex.ecf.screen.*;
import org.tetawex.ecf.tutorial.*;
import org.tetawex.ecf.util.Bundle;

/**
 * Created by Tetawex on 30.12.2016.
 */
public class GameStateManager {
    public void dispose() {
        currentScreen.dispose();
    }

    public enum GameState {MAIN_MENU, GAME, SETTINGS, HIGHSCORES, MODE_SELECT, LEVEL_SELECT, TUTORIAL, EDITOR, MOT_TUTORIAL, LEVEL_PACK_SELECT}

    private BaseScreen<ECFGame> currentScreen;
    private GameState currentState;
    private ECFGame game;

    public GameStateManager(ECFGame game) {
        this.game = game;
        setState(GameState.MAIN_MENU, null);
    }

    public GameStateManager(ECFGame game, GameState state) {
        this(game);
        setState(state, null);
    }

    public BaseScreen<ECFGame> getCurrentScreen() {
        return currentScreen;
    }

    public void renderCurrentScreen(float deltaTime) {
        currentScreen.render(deltaTime);
    }

    public void setState(GameState gameState, Bundle bundle) {
        currentState = gameState;
        if (currentScreen != null)
            currentScreen.dispose();

        switch (currentState) {
            case TUTORIAL:
                currentScreen = new org.tetawex.ecf.tutorial.TutorialScreen(game, bundle);
                break;
            case MOT_TUTORIAL:
                currentScreen = new MotTutorialScreen(game, bundle);
                break;
            case MODE_SELECT:
                currentScreen = new PlayModeSelectScreen(game, bundle);
                break;
            case LEVEL_SELECT:
                currentScreen = new LevelSelectScreen(game, bundle);
                break;
            case HIGHSCORES:
                currentScreen = new HighscoresScreen(game, bundle);
                break;
            case MAIN_MENU:
                currentScreen = new MainMenuScreen(game, bundle);
                break;
            case SETTINGS:
                currentScreen = new SettingsScreen(game, bundle);
                break;
            case GAME:
                currentScreen = new GameScreen(game, bundle);
                break;
            case LEVEL_PACK_SELECT:
                currentScreen = new LevelPackSelectScreen(game, bundle);
                break;
            case EDITOR:
                currentScreen = new EditorScreen(game, bundle);
                break;
        }
    }
}
