package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.LevelIconActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.LevelData;
import org.tetawex.ecf.model.LevelFactory;
import org.tetawex.ecf.model.Score;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class LevelSelectScreen extends BaseScreen<ECFGame> {
    private static final float BUTTON_WIDTH =1275f;
    private static final float BUTTON_HEIGHT =255f;
    private static final float BUTTON_PAD =40f;
    private static final float BUTTON_FONT_SCALE =1f;
    private static final float LABEL_FONT_SCALE=1f;

    private ECFPreferences preferences;

    private Stage stage;

    public LevelSelectScreen(ECFGame game, Bundle bundle) {
        super(game);
        Camera camera=new OrthographicCamera(1440f,2560f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        stage=new Stage(new ExtendViewport(1440f,2560f,camera));
        Gdx.input.setInputProcessor(stage);
        preferences= PreferencesProvider.getPreferences();
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
        Table levelTable=new Table();
        int q=0;
        for (int i=0;i<9;i++) {
            LevelIconActor actor=new LevelIconActor(getGame(),
                    preferences.getLevelCompletionStateList().get(i),
                    getGame().getAssetManager().get("fonts/font_main_medium.ttf",BitmapFont.class),
                    i+1);
            levelTable.add(actor).pad(40).left();
            final int finalI = i;
            actor.addListener(new InputListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y,
                                    int pointer, int button) {

                }

                public boolean touchDown(InputEvent event, float x, float y,
                                         int pointer, int button) {
                    if(preferences.getLevelCompletionStateList().get(finalI).isUnlocked()) {
                        Bundle bundle = new Bundle();
                        bundle.putItem("levelData", LevelFactory.generateLevel(finalI));
                        getGame().getGameStateManager().setState(GameStateManager.GameState.GAME, bundle);
                    }
                    return true;
                }

            });
            q++;
            if(q>2) {
                levelTable.row();
                q=0;
            }
            if(i>9)
                break;
        }
        mainTable.add(levelTable).pad(40f).row();
        TextButton menuButtonBackToMainMenu=
                new TextButton(getGame().getLocalisedString("back"), org.tetawex.ecf.screen.StyleFactory.generateStandardMenuButtonSkin(getGame()));
        menuButtonBackToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT,null);
            }
        });
        menuButtonBackToMainMenu.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH,BUTTON_HEIGHT).pad(BUTTON_PAD);
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
