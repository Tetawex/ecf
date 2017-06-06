package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.util.Bundle;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class MainMenuScreen extends BaseScreen<ECFGame> {
    private static final float BUTTON_WIDTH =1275f;
    private static final float BUTTON_HEIGHT =252f;
    private static final float BUTTON_PAD =40f;
    private static final float BUTTON_FONT_SCALE =1f;
    private static final float LABEL_FONT_SCALE=1f;
    private Stage stage;
    private ECFPreferences preferences;

    public MainMenuScreen(ECFGame game,Bundle bundle) {
        super(game);
        Camera camera=new OrthographicCamera(1440f,2560f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        stage=new Stage(new ExtendViewport(1440f,2560f,camera));
        Gdx.input.setInputProcessor(stage);
        initUi();
    }
    private void initUi() {
        Stack stack=new Stack();
        stack.setFillParent(true);
        stack.add(new Image(
                getGame().getAssetManager()
                        .get("backgrounds/background.png",
                                Texture.class)));
        Table titleTable=new Table();
        titleTable.add(new Image(
                getGame().getAssetManager()
                        .get("backgrounds/text_logo.png",
                                Texture.class))).size(1270,134).padTop(300);
        titleTable.top();
        Table mainTable=new Table();
        mainTable.setFillParent(true);
        stack.add(mainTable);
        stack.add(titleTable);
        stage.addActor(stack);
        TextButton menuButtonPlay=
                new TextButton(getGame().getLocalisedString("play"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        menuButtonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT,null);
            }
        });
        menuButtonPlay.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonPlay)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();

        TextButton menuButtonHighscores=
                new TextButton(getGame().getLocalisedString("highscores"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        menuButtonHighscores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.HIGHSCORES,null);
            }
        });
        menuButtonHighscores.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonHighscores)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();

        TextButton menuButtonSettings=
                new TextButton(getGame().getLocalisedString("settings"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        menuButtonSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.SETTINGS,null);
            }
        });
        menuButtonSettings.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonSettings)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();

        TextButton menuButtonQuit=
                new TextButton(getGame().getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        menuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        menuButtonQuit.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonQuit)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
        stage.getViewport().getCamera().update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
