package org.tetawex.ecf.actor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Created by Tetawex on 17.05.2017.
 */
public class BaseWidget<T extends Game> extends Widget{
    private T game;
    public BaseWidget(T game){
        this.game=game;
    }


    public T getGame() {
        return game;
    }
    public void setGame(T game) {
        this.game = game;
    }
}
