package org.tetawex.ecf.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.tetawex.ecf.core.screen.GameScreen;
import org.tetawex.ecf.core.screen.MainMenuScreen;
import org.tetawex.ecf.core.screen.SettingsScreen;
import org.tetawex.ecf.util.Bundle;

/**
 * Created by Tetawex on 30.12.2016.
 */
public class GameStateManager
{
    public enum GameState{MAIN_MENU,GAME,SETTINGS,}

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
            case MAIN_MENU:
                currentScreen=new MainMenuScreen(game,bundle);
                break;
            case SETTINGS:
                currentScreen=new SettingsScreen(game,bundle);
                break;
            case GAME:
                currentScreen=new GameScreen(game,bundle);
                break;
        }
    }
}
