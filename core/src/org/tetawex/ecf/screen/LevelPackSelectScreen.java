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
import org.tetawex.ecf.model.BaseECFScreen;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class LevelPackSelectScreen extends BaseECFScreen {
    private static final float BUTTON_WIDTH = 1275f;
    private static final float BUTTON_HEIGHT = 255f;
    private static final float BUTTON_PAD = 40f;
    private static final float BUTTON_FONT_SCALE = 1f;
    private static final float LABEL_FONT_SCALE = 1f;

    private ECFPreferences preferences;

    private Stage stage;

    public LevelPackSelectScreen(ECFGame game, Bundle bundle) {
        super(game);
        preferences = PreferencesProvider.getPreferences();
        if (!preferences.isCompletedTutorial())
            getGame().getGameStateManager().setState(GameStateManager.GameState.TUTORIAL, null);
        Camera camera = new OrthographicCamera(1440f, 2560f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        stage = new Stage(new ExtendViewport(1440f, 2560f, camera));
        Gdx.input.setInputProcessor(stage);
        initUi();
    }

    private void initUi() {
        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(new Image(
                getGame().getAssetManager()
                        .get("backgrounds/background.png",
                                Texture.class)));
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stack.add(mainTable);
        stage.addActor(stack);
        TextButton.TextButtonStyle tbStyle = StyleFactory.generateStandardMenuButtonStyle(getGame());
        TextButton menuButtonClassic =
                new TextButton(getGame().getLocalisedString("lp_classic"), tbStyle);
        menuButtonClassic.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Bundle bundle = new Bundle();
                bundle.putItem("levelCode", "");
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, bundle);
            }
        });
        TextButton menuButtonMot =
                new TextButton(getGame().getLocalisedString("lp_mot"), StyleFactory.generateMotMenuButtonStyle(getGame()));
        menuButtonMot.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(preferences.isCompletedMotTutorial()) {
                    Bundle bundle = new Bundle();
                    bundle.putItem("levelCode", "mot");
                    getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, bundle);
                }
                else
                    getGame().getGameStateManager().setState(GameStateManager.GameState.MOT_TUTORIAL, null);
            }
        });
        TextButton menuButtonBackPlayModeSelectScreen =
                new TextButton(getGame().getLocalisedString("back"), tbStyle);
        menuButtonBackPlayModeSelectScreen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onBackPressed();
            }
        });
        mainTable.add(menuButtonClassic).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row();
        mainTable.add(menuButtonMot).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row();
        mainTable.add(menuButtonBackPlayModeSelectScreen).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD).row();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getViewport().getCamera().update();
    }

    @Override
    public void onBackPressed() {
        getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT, null);
    }
}
