package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.EditorHexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * ...
 */
public class EditorScreen extends BaseECFScreen {
    public enum ButtonAction {
        ADD_FIRE, ADD_WATER, ADD_AIR, ADD_EARTH, ADD_SHADOW, ADD_LIGHT, ADD_TIME,
        REMOVE_CELL, PLAY_LEVEL, SAVE_LEVEL, LOAD_LEVEL;

        public static Array<ButtonAction> getAllActions() {
            Array<ButtonAction> array = new Array<ButtonAction>();
            array.addAll(values());
            return array;
        }

        public static Array<ButtonAction> getTopRowActions() {
            Array<ButtonAction> array = new Array<ButtonAction>();
            array.addAll(ADD_FIRE, ADD_WATER, ADD_AIR, ADD_EARTH, ADD_SHADOW, ADD_LIGHT, ADD_TIME,
                    REMOVE_CELL);
            return array;
        }

        public static Array<ButtonAction> getBottomRowActions() {
            Array<ButtonAction> array = new Array<ButtonAction>();
            array.addAll(PLAY_LEVEL, SAVE_LEVEL, LOAD_LEVEL);
            return array;
        }
    }

    private static final float PAUSE_BUTTON_WIDTH = 1275f;
    private static final float PAUSE_BUTTON_HEIGHT = 252f;
    private static final float PAUSE_BUTTON_PAD = 32f;
    private static final float PAUSE_BUTTON_FONT_SCALE = 1f;
    private static final float MANA_LABEL_FONT_SCALE = 1f;
    private static final float SCORE_LABEL_FONT_SCALE = 1f;
    private static final float ELEMENT_COUNTER_IMAGE_SIZE = 130f;

    private LevelData levelData;

    private Stage gameStage;
    private EditorHexMapActor hexMapActor;

    Table pauseTable;

    private ECFPreferences preferences;

    private Map<GameData.LossCondition, String> lcToStringMap;
    private Image[] stars;
    private String levelCode;

    public EditorScreen(ECFGame game, Bundle bundle) {
        super(game);

        Camera camera = new OrthographicCamera(1440f, 2560f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        gameStage = new Stage(new ExtendViewport(1440f, 2560f, camera));

        Gdx.input.setInputProcessor(gameStage);

        //levelData = bundle.getItem("levelData", LevelData.class);
        levelData = LevelFactory.generateMotTestingGround();
        levelCode = levelData.getLevelCode();

        initUi();

        preferences = PreferencesProvider.getPreferences();

        lcToStringMap = new HashMap<GameData.LossCondition, String>();
        lcToStringMap.put(GameData.LossCondition.NO_MANA, "lc_no_mana");

        lcToStringMap.put(GameData.LossCondition.NO_FIRE, "lc_no_fire");
        lcToStringMap.put(GameData.LossCondition.NO_WATER, "lc_no_water");

        lcToStringMap.put(GameData.LossCondition.NO_AIR, "lc_no_air");
        lcToStringMap.put(GameData.LossCondition.NO_EARTH, "lc_no_earth");

        lcToStringMap.put(GameData.LossCondition.NO_SHADOW, "lc_no_shadow");
        lcToStringMap.put(GameData.LossCondition.NO_LIGHT, "lc_no_light");

        lcToStringMap.put(GameData.LossCondition.NO_TIME, "lc_no_light");
    }

    @Override
    public void render(float delta) {
        gameStage.act();
        gameStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
        gameStage.getViewport().getCamera().update();
    }

    private void initUi() {
        pauseTable = new Table();
        pauseTable.setVisible(false);

        Table mainTable = new Table();
        Stack stack = new Stack();
        stack.setFillParent(true);

        final Image background = new Image(getGame().getAssetManager()
                .get("backgrounds/" + levelCode + "background.png", Texture.class));
        background.setFillParent(true);
        final Image backgroundPause = new Image(getGame().getAssetManager()
                .get("backgrounds/background_pause.png", Texture.class));
        backgroundPause.setFillParent(true);
        backgroundPause.setVisible(false);

        stack.add(background);
        stack.add(mainTable);
        stack.add(backgroundPause);
        stack.add(pauseTable);

        gameStage.addActor(stack);

        Table topRowTable = new Table();
        Table topRowLeftTable = new Table();
        Table topRowCenterTable = new Table();
        Table topRowRightTable = new Table();
        topRowTable.add(topRowLeftTable).width(300f);
        topRowTable.add(topRowCenterTable).growX();
        topRowTable.add(topRowRightTable).width(300f);
        Table midRowTable = new Table();
        Table bottomRowTable = new Table();

        hexMapActor = new EditorHexMapActor(getGame());
        hexMapActor.setSoundVolume(PreferencesProvider.getPreferences().getSoundVolume());
        hexMapActor.setCellArray(CellArrayFactory.generateEmptyCellArray(2,2));

        midRowTable.add(hexMapActor).center().expand();

        mainTable.setFillParent(true);
        mainTable.add(new Label(levelData.getName(), SkinFactory.generateStandardLabelSkin(getGame()))).row();
        mainTable.add(midRowTable).growX().growY().row();
        mainTable.add(bottomRowTable).growX();

        TextButton pauseButton = new TextButton(" ", SkinFactory.generatePauseButtonSkin(getGame()));
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
            }
        });
        topRowLeftTable.left().top();
        topRowLeftTable.add(pauseButton).size(120f).pad(40f).center();

        //element + null buttons
        Table elementTable = new Table();
        for (ButtonAction action : ButtonAction.getTopRowActions()) {
            elementTable.add(generateButtonForAction(action)).pad(PAUSE_BUTTON_PAD/4);
        }
        elementTable.row();
        bottomRowTable.add(elementTable);

        //pause ui
        pauseTable.center();

        TextButton pauseMenuButtonContinue =
                new TextButton(getGame().getLocalisedString("continue"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonContinue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
            }
        });
        pauseMenuButtonContinue.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonContinue)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton pauseMenuButtonRetry =
                new TextButton(getGame().getLocalisedString("retry"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
            }
        });
        pauseMenuButtonRetry.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();


        TextButton pauseMenuButtonQuit =
                new TextButton(getGame().getLocalisedString("quit"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                if (levelData.getLevelNumber() != -1) {
                    Bundle bundle = new Bundle();
                    bundle.putItem("levelCode", levelCode);
                    getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, bundle);
                } else
                    getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT, null);
            }
        });
        pauseMenuButtonQuit.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onBackPressed() {
        if (pauseTable.isVisible())
            pauseTable.setVisible(false);
        else {
            if (levelData.getLevelNumber() != -1)
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
            else
                getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT, null);
        }
    }

    public TextButton generateButtonForAction(ButtonAction action) {
        TextButton button = new TextButton("", SkinFactory.generateActionButtonSkin(getGame(), action));

        switch (action) {
            case ADD_FIRE:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.FIRE);
                    }
                });
                break;
            case ADD_WATER:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.WATER);
                    }
                });
                break;
            case ADD_AIR:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.AIR);
                    }
                });
                break;
            case ADD_EARTH:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.EARTH);
                    }
                });
                break;
            case ADD_SHADOW:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.SHADOW);
                    }
                });
                break;
            case ADD_LIGHT:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.LIGHT);
                    }
                });
                break;
            case ADD_TIME:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.addElementToSelectedCell(Element.TIME);
                    }
                });
                break;
            case REMOVE_CELL:
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        hexMapActor.removeOrCreateCell();
                    }
                });
                break;
            case SAVE_LEVEL:
                break;
            case LOAD_LEVEL:
                break;
            case PLAY_LEVEL:
                break;

        }

        return button;
    }
}
