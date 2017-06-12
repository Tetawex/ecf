package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.LevelData;
import org.tetawex.ecf.model.RandomLevelFactory;
import org.tetawex.ecf.model.Score;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

import java.util.Random;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class PlayModeSelectScreen extends BaseScreen<ECFGame> {
    private static final float BUTTON_WIDTH =1275f;
    private static final float BUTTON_HEIGHT =255f;
    private static final float BUTTON_PAD =40f;
    private static final float BUTTON_FONT_SCALE =1f;
    private static final float LABEL_FONT_SCALE=1f;

    private ECFPreferences preferences;

    private Stage stage;

    public PlayModeSelectScreen(ECFGame game, Bundle bundle) {
        super(game);
        preferences= PreferencesProvider.getPreferences();
        if(!preferences.isCompletedTutorial())
            getGame().getGameStateManager().setState(GameStateManager.GameState.TUTORIAL,null);
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
        Table mainTable=new Table();
        mainTable.setFillParent(true);
        stack.add(mainTable);
        stage.addActor(stack);
        TextButton.TextButtonStyle tbStyle=StyleFactory.generateStandardMenuButtonSkin(getGame());
        TextButton menuButtonLevels=
                new TextButton(getGame().getLocalisedString("levels"), tbStyle);
        menuButtonLevels.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT,null);
            }
        });
        TextButton menuButtonRandom=
                new TextButton(getGame().getLocalisedString("random"), tbStyle);
        menuButtonRandom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Bundle bundle=new Bundle();
                RandomLevelFactory factory=new RandomLevelFactory(new Random().nextInt(10000),new Random().nextInt(2)+3,new Random().nextInt(3)+3);
                bundle.putItem("levelData",new LevelData(factory.getTheBoard(),factory.getMana(),10000,-1,"Generated Level"));
                getGame().getGameStateManager().setState(GameStateManager.GameState.GAME,bundle);
            }
        });
        TextButton menuButtonTutorial=
                new TextButton(getGame().getLocalisedString("how_to_play"), tbStyle);
        menuButtonTutorial.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.TUTORIAL,null);
            }
        });
        TextButton menuButtonBackToMainMenu=
                new TextButton(getGame().getLocalisedString("back"), tbStyle);
        menuButtonBackToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU,null);
            }
        });
        mainTable.add(menuButtonLevels).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(BUTTON_PAD).row();
        mainTable.add(menuButtonRandom).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(BUTTON_PAD).row();
        mainTable.add(menuButtonTutorial).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(BUTTON_PAD).row();
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(BUTTON_PAD).row();
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
}
