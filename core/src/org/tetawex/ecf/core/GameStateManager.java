package org.tetawex.ecf.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.tetawex.ecf.core.screen.GameScreen;

/**
 * Created by Tetawex on 30.12.2016.
 */
public class GameStateManager
{
    public enum GameState{MAIN_MENU,GAME,SETTINGS,GAME_OVER}

    private Screen currentScreen;
    private GameState currentState;
    private ECFGame game;

    public GameStateManager(ECFGame game)
    {
        this.game=game;
        setState(GameState.GAME);
    }
    public GameStateManager(ECFGame game, GameState state)
    {
        this(game);
        setState(state);
    }

    public Screen getCurrentScreen()
    {
        return currentScreen;
    }

    public void renderCurrentScreen(float deltaTime){
        currentScreen.render(deltaTime);
    }

    public void setState(GameState gameState){
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
            case GAME:
                currentScreen=new GameScreen(game);
        }
    }
}
