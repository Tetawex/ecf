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
import org.tetawex.ecf.model.Score;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class HighscoresScreen extends BaseScreen<ECFGame> {
    private static final float BUTTON_WIDTH = 1275f;
    private static final float BUTTON_HEIGHT = 255f;
    private static final float BUTTON_PAD = 40f;
    private static final float BUTTON_FONT_SCALE = 1f;
    private static final float LABEL_FONT_SCALE = 1f;

    private ECFPreferences preferences;

    private Stage stage;

    public HighscoresScreen(ECFGame game, Bundle bundle) {
        super(game);
        Camera camera = new OrthographicCamera(1440f, 2560f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        stage = new Stage(new ExtendViewport(1440f, 2560f, camera));
        Gdx.input.setInputProcessor(stage);
        preferences = PreferencesProvider.getPreferences();
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
        Label.LabelStyle labelStyle = StyleFactory.generateStandardLabelStyle(getGame());
        int i = 1;
        for (Score score : preferences.getScores()) {
            Table table = new Table();
            table.add(new Label(String.valueOf(i), labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f);
            table.add(new Label(score.getName(), labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f).growX();
            table.add(new Label(String.valueOf(score.getValue()), labelStyle))
                    .padLeft(80f).padRight(80f).padBottom(40f).padTop(40f);
            mainTable.add(table).growX().row();
            i++;
            if (i > 9)
                break;
        }
        TextButton menuButtonBackToMainMenu =
                new TextButton(getGame().getLocalisedString("back"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        menuButtonBackToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onBackPressed();
            }
        });
        menuButtonBackToMainMenu.getLabel().setFontScale(BUTTON_FONT_SCALE);
        mainTable.add(menuButtonBackToMainMenu).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(BUTTON_PAD);
    }

    @Override
    public void onBackPressed() {
        getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU, null);
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
}
