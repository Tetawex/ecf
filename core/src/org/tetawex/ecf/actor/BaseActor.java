package org.tetawex.ecf.actor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Tetawex on 17.05.2017.
 */
public class BaseActor<T extends Game> extends Actor {
    private T game;

    public BaseActor(T game) {
        this.game = game;
    }


    public T getGame() {
        return game;
    }

    public void setGame(T game) {
        this.game = game;
    }
}
