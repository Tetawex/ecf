package org.tetawex.ecf.core;

import com.badlogic.gdx.Screen;
import org.tetawex.ecf.util.Bundle;

/**
 * Created by Tetawex on 30.12.2016.
 */
public class GameStateManager
{
    public void dispose() {
        currentScreen.dispose();
    }

    public enum GameState{MAIN_MENU,GAME,SETTINGS,HIGHSCORES,MODE_SELECT,LEVEL_SELECT,TUTORIAL}

    private Screen currentScreen;
    private GameState currentState;
    private ECFGame game;

    public GameStateManager(ECFGame game)
    {
        this.game=game;
        setState(GameState.MAIN_MENU,null);
    }
    public GameStateManager(ECFGame game, GameState state)
    {
        this(game);
        setState(state,null);
    }

    public Screen getCurrentScreen()
    {
        return currentScreen;
    }

    public void renderCurrentScreen(float deltaTime){
        currentScreen.render(deltaTime);
    }

    public void setState(GameState gameState,Bundle bundle){
        currentState=gameState;
        if(currentScreen!=null)
            currentScreen.dispose();

        switch (currentState)
        {
            /*case MAIN_MENU:
                currentScreen=new MainMenuScreen(game);
                break;
            case GAME:
                currentScreen=new GameScreen(game);
                break;
            case GAME_OVER:
                currentScreen=new ScreenAdapter();
                break;
            case SETTINGS:
                currentScreen=new ScreenAdapter();
                break;*/
            case TUTORIAL:
                currentScreen=new org.tetawex.ecf.screen.TutorialScreen(game,bundle);
                break;
            case MODE_SELECT:
                currentScreen=new org.tetawex.ecf.screen.PlayModeSelectScreen(game,bundle);
                break;
            case LEVEL_SELECT:
                currentScreen=new org.tetawex.ecf.screen.LevelSelectScreen(game,bundle);
                break;
            case HIGHSCORES:
                currentScreen=new org.tetawex.ecf.screen.HighscoresScreen(game,bundle);
                break;
            case MAIN_MENU:
                currentScreen=new org.tetawex.ecf.screen.MainMenuScreen(game,bundle);
                break;
            case SETTINGS:
                currentScreen=new org.tetawex.ecf.screen.SettingsScreen(game,bundle);
                break;
            case GAME:
                currentScreen=new org.tetawex.ecf.screen.GameScreen(game,bundle);
                break;
        }
    }
}
