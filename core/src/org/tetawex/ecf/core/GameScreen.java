package org.tetawex.ecf.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * ...
 */
public class GameScreen extends ScreenAdapter {
    private Stage gameStage;
    public GameScreen(ECFGame game){
        Camera camera=new OrthographicCamera(320f,180f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        gameStage=new Stage(new ExtendViewport(2560,1440,camera));
    }
    @Override
    public void render(float delta){
        gameStage.act();
        gameStage.draw();
    }
}
