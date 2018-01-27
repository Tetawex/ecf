package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.Language;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.FontCharacters;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class SettingsScreen extends BaseScreen<ECFGame> {
    private static final float BUTTON_WIDTH = 1275f;
    private static final float BUTTON_HEIGHT = 255f;
    private static final float BUTTON_HEIGHT_HALVED = 120f;
    private static final float BUTTON_PAD = 40f;
    private static final float BUTTON_FONT_SCALE = 1f;
    private static final float LABEL_FONT_SCALE = 1f;

    private ECFPreferences preferences;

    private Stage stage;
    private Slider musicSlider;
    private Slider soundSlider;
    private Label selectedLanguageLabel;

    public SettingsScreen(ECFGame game, Bundle bundle) {
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

        //Sound table
        Table soundTable = new Table();
        soundSlider = new Slider(
                0f, 1f, 0.0001f, false,
                StyleFactory.generateStandardSliderStyle(getGame()));
        soundSlider.setValue(preferences.getSoundVolume());
        soundTable.add(new Label(getGame().getLocalisedString("sound"),
                StyleFactory.generateStandardLabelStyle(getGame()))).width(250f).padRight(40f);
        soundTable.add(new Image(getGame().getTextureRegionFromAtlas("sound_off"))).size(86f, 86f);
        soundTable.add(soundSlider).growX().pad(40f);
        soundTable.add(new Image(getGame().getTextureRegionFromAtlas("sound_on"))).size(86f, 86f);
        soundTable.padRight(80f);
        soundTable.padLeft(80f);
        //Music table
        Table musicTable = new Table();
        musicSlider = new Slider(
                0f, 1f, 0.0001f, false,
                StyleFactory.generateStandardSliderStyle(getGame()));
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().setMusicVolume(musicSlider.getValue());
            }
        });
        musicSlider.setValue(preferences.getMusicVolume());
        musicTable.add(new Label(getGame().getLocalisedString("music"), StyleFactory.generateStandardLabelStyle(getGame()))).width(250f).padRight(40f);
        musicTable.add(new Image(getGame().getTextureRegionFromAtlas("music_off"))).size(86f, 86f);
        musicTable.add(musicSlider).growX().pad(40f);
        musicTable.add(new Image(getGame().getTextureRegionFromAtlas("music_on"))).size(86f, 86f);
        musicTable.padRight(80f);
        musicTable.padLeft(80f);
        //Language table
        selectedLanguageLabel = new Label(FontCharacters.codeToLanguageMap.get(preferences.getSelectedLanguage()),
                StyleFactory.generateDarkerLabelStyle(getGame()));
        Table languageTable = new Table();
        languageTable.add(new Label(getGame().getLocalisedString("language"), StyleFactory.generateStandardLabelStyle(getGame())));
        Table secondaryLanguageTable = new Table();
        secondaryLanguageTable.add(selectedLanguageLabel).pad(40f);
        secondaryLanguageTable.add(new Image(getGame().getTextureRegionFromAtlas("arrow"))).size(35f, 57f);
        languageTable.add(secondaryLanguageTable).growX();
        secondaryLanguageTable.right();
        languageTable.padRight(80f);
        languageTable.padLeft(80f);
        //Back button
        TextButton menuButtonBackToMainMenu =
                new TextButton(getGame().getLocalisedString("back"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        menuButtonBackToMainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onBackPressed();
            }
        });
        menuButtonBackToMainMenu.getLabel().setFontScale(BUTTON_FONT_SCALE);
        //Dumping everything into the main table
        mainTable.add(soundTable).growX().row();
        mainTable.add(musicTable).growX().row();
        mainTable.add(languageTable).growX().row();
        mainTable.add(menuButtonBackToMainMenu)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();
        //Initializing language list
        final Stack langStack = new Stack();
        langStack.setFillParent(true);
        langStack.setVisible(false);
        langStack.add(new Image(
                getGame().getAssetManager()
                        .get("backgrounds/background.png",
                                Texture.class)));
        Table languageSelectTable = new Table();
        langStack.add(languageSelectTable);
        stack.add(langStack);
        Table languageListTable = new Table();
        languageSelectTable.add(languageListTable).pad(80f).row();
        TextButton languageSelectBackButton =
                new TextButton(getGame().getLocalisedString("back"),
                        StyleFactory.generateStandardMenuButtonStyle(getGame()));
        languageSelectBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                langStack.setVisible(false);
            }
        });
        languageSelectTable.add(languageSelectBackButton)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .center().pad(BUTTON_PAD).row();

        TextButton defaultButton = new TextButton(getGame().getLocalisedString("auto"), StyleFactory.generateLanguageMenuButtonStyle(getGame()));
        defaultButton.left();
        languageListTable.add(defaultButton).size(BUTTON_WIDTH, BUTTON_HEIGHT_HALVED)
                .center().pad(BUTTON_PAD).row();
        defaultButton.getLabel().setAlignment(Align.left);
        defaultButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                preferences.setSelectedLanguageCode("default");
                preferences.setSelectedCountryCode("");
                PreferencesProvider.flushPreferences();
                getGame().setupLocalisation();
                getGame().getGameStateManager().setState(GameStateManager.GameState.SETTINGS, null);
            }
        });
        for (final Language language : FontCharacters.getSupportedLanguages()) {
            TextButton button = new TextButton(language.getName(), StyleFactory.generateLanguageMenuButtonStyle(getGame()));
            button.getLabel().setAlignment(Align.left);
            languageListTable.add(button)
                    .size(BUTTON_WIDTH, BUTTON_HEIGHT_HALVED)
                    .center().pad(BUTTON_PAD).row();
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    preferences.setSelectedLanguage(language.getCode());
                    PreferencesProvider.flushPreferences();
                    getGame().setupLocalisation();
                    getGame().getGameStateManager().setState(GameStateManager.GameState.SETTINGS, null);
                }
            });
        }
        languageTable.setTouchable(Touchable.enabled);
        languageTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                langStack.setVisible(true);
            }
        });
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
        stage.getViewport().update(width, height, true);
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
    public void onBackPressed() {
        getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU, null);
    }

    @Override
    public void dispose() {
        preferences.setMusicVolume(musicSlider.getValue());
        preferences.setSoundVolume(soundSlider.getValue());
    }
}
