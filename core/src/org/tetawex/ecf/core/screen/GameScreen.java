package org.tetawex.ecf.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.HexMapActor;
import org.tetawex.ecf.core.ECFGame;

/**
 * ...
 */
public class GameScreen extends BaseScreen<ECFGame> {
    private Stage gameStage;
    public GameScreen(ECFGame game){
        super(game);

        Camera camera=new OrthographicCamera(25.6f,14.4f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        gameStage=new Stage(new ExtendViewport(25.6f,14.4f,camera));

        Music music=Gdx.audio.newMusic(Gdx.files.internal("music/ambient.ogg"));
        music.setLooping(true);
        music.play();

        Gdx.input.setInputProcessor(gameStage);

        initUi();
    }
    private void initUi(){

        Table mainTable=new Table();
        Stack stack=new Stack();
        stack.setFillParent(true);
        Image background=new Image(getGame().getAssetManager().get("backgrounds/background.png", Texture.class));
        background.setFillParent(true);
        stack.add(background);
        stack.add(mainTable);
        gameStage.addActor(stack);
        Table topRowTable = new Table();
        Table midRowTable = new Table();
        Table bottomRowTable = new Table();
        midRowTable.add(new HexMapActor(getGame(),3,5)).center().expand();

        mainTable.setFillParent(true);
        mainTable.add(topRowTable).expandX().prefHeight(2.5f).left().row();
        mainTable.add(midRowTable).expandX().expandY().row();
        mainTable.add(bottomRowTable).expandX().prefHeight(2.5f);
    }
    @Override
    public void render(float delta){
        gameStage.act();
        gameStage.draw();
    }
    @Override
    public void resize(int width,int height){
        gameStage.getViewport().update(width,height,true);
        gameStage.getViewport().getCamera().update();
    }
}
