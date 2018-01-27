package org.tetawex.ecf.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import org.tetawex.ecf.core.ECFGame;

/**
 * Created by Tetawex on 17.05.2017.
 */
public class BaseScreen<T extends Game> extends ScreenAdapter {
    private T game;

    public BaseScreen(T game) {
        this.game = game;
    }

    public T getGame() {
        return game;
    }

    public void setGame(T game) {
        this.game = game;
    }

    public void onBackPressed() {
    }
}
