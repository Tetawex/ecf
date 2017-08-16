package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.EditorHexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.util.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ...
 */
public class EditorScreen extends BaseECFScreen {
    public static String EDITOR_LEVEL_STORAGE = "Elementality/Levels/";

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

    private Stage gameStage;
    private EditorHexMapActor hexMapActor;

    private TextField nameField;
    private TextField dimXField;
    private TextField dimYField;
    private TextField maxScoreField;
    private TextField manaField;

    Table pauseTable;
    Table midRowTable;

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
        LevelData levelData = LevelFactory.generateMotTestingGround();
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
        midRowTable = new Table();
        Table bottomRowTable = new Table();
        mainTable.add(topRowTable).growX().row();

        hexMapActor = new EditorHexMapActor(getGame());
        hexMapActor.setSoundVolume(PreferencesProvider.getPreferences().getSoundVolume());
        hexMapActor.setCellArray(CellArrayFactory.generateEmptyCellArray(2, 2));

        midRowTable.add(hexMapActor).center().expand();

        mainTable.setFillParent(true);
        mainTable.add(midRowTable).growX().growY().row();
        mainTable.add(bottomRowTable).growX();

        TextButton pauseButton = new TextButton(" ", StyleFactory.generatePauseButtonStyle(getGame()));
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
            elementTable.add(generateButtonForAction(action)).size(142).pad(PAUSE_BUTTON_PAD / 4);
        }
        bottomRowTable.add(elementTable).pad(16f).growX().row();

        bottomRowTable.add(createLevelSpecsTable()).pad(PAUSE_BUTTON_PAD).grow().row();
        bottomRowTable.add(createLevelResizeTable()).pad(PAUSE_BUTTON_PAD).padBottom(32f).grow();

        //pause ui
        pauseTable.center();

        TextButton pauseMenuButtonContinue =
                new TextButton(getGame().getLocalisedString("continue"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
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
        //save
        TextButton pauseMenuButtonSaveLevel =
                new TextButton(getGame().getLocalisedString("save"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        pauseMenuButtonSaveLevel.addListener(new ChangeListener() {
            volatile boolean busy;

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!getGame().getActionResolver().externalStorageAccessible())
                    return;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (busy)
                            return;
                        busy = true;
                        try {
                            LevelData levelData;
                            levelData = createLevelData();

                            FileHandle handle = Gdx.files.external(EDITOR_LEVEL_STORAGE +
                                    levelData.getName() + ".json");
                            handle.writeString(LevelDataUtils.toJson(levelData), false);
                        } catch (Exception e) {
                        }
                        busy = false;
                    }
                });
                thread.setDaemon(false);
                thread.run();

            }
        });
        pauseMenuButtonSaveLevel.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonSaveLevel)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();
        //load
        TextButton pauseMenuButtonLoadLevel =
                new TextButton(getGame().getLocalisedString("load"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        pauseMenuButtonLoadLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!getGame().getActionResolver().externalStorageAccessible())
                    return;
                FileChooser files = new FileChooser(
                        "",
                        StyleFactory.generateFileChooserStyle(getGame()),
                        getGame()) {
                    @Override
                    protected void result(Object object) {
                        if (object instanceof FileHandle) {
                            try {
                                FileHandle file = getFile();

                                LevelData levelData = LevelDataUtils.fromJson(file.readString());
                                resolveLoadedLevelData(levelData);
                            } catch (Exception e) {
                            }
                        }

                    }
                };
                files.setDirectory(Gdx.files.external(EDITOR_LEVEL_STORAGE));
                files.show(gameStage);
            }
        });
        pauseMenuButtonLoadLevel.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonLoadLevel)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();


        TextButton pauseMenuButtonQuit =
                new TextButton(getGame().getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        pauseMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
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
            getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT, null);
        }
    }

    public TextButton generateButtonForAction(ButtonAction action) {
        TextButton button = new TextButton("", StyleFactory.generateActionButtonStyle(getGame(), action));

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

    private Table createLevelSpecsTable() {
        Table placeholder = new Table();
        nameField = new TextField("", StyleFactory.generateEditorTextFieldStyle(getGame()));
        nameField.setAlignment(Align.center);
        nameField.setMessageText(getGame().getLocalisedString("hint_name"));

        maxScoreField = new TextField("1000", StyleFactory.generateEditorTextFieldStyle(getGame()));
        maxScoreField.setAlignment(Align.center);
        maxScoreField.setMessageText(getGame().getLocalisedString("hint_max_score"));

        manaField = new TextField("1", StyleFactory.generateEditorTextFieldStyle(getGame()));
        manaField.setAlignment(Align.center);
        manaField.setMessageText(getGame().getLocalisedString("hint_mana"));

        placeholder.add(nameField).growX().padRight(16f);
        placeholder.add(maxScoreField).width(512).padRight(16f);
        placeholder.add(manaField).width(256).row();
        return placeholder;

    }

    private Table createLevelResizeTable() {
        Table placeholder = new Table();

        dimXField = new TextField("", StyleFactory.generateEditorTextFieldStyle(getGame()));
        dimXField.setAlignment(Align.center);
        dimXField.setText("2");
        dimXField.setMessageText(getGame().getLocalisedString("hint_width"));

        dimYField = new TextField("", StyleFactory.generateEditorTextFieldStyle(getGame()));
        dimYField.setAlignment(Align.center);
        dimYField.setText("2");
        dimYField.setMessageText(getGame().getLocalisedString("hint_height"));

        TextButton applyButton = new TextButton(getGame().getLocalisedString("apply"),
                StyleFactory.generateStandardMenuButtonStyle(getGame()));
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    hexMapActor.setHexSize(new IntVector2(
                            Integer.valueOf(dimXField.getText()),
                            Integer.valueOf(dimYField.getText())));
                    midRowTable.reset();
                    midRowTable.add(hexMapActor).center().expand();

                } catch (Exception e) {
                    dimXField.setText(String.valueOf(hexMapActor.getCellArray().length));
                    dimYField.setText(String.valueOf(hexMapActor.getCellArray()[0].length));
                }
            }
        });

        placeholder.add(dimXField).width(400).padRight(16f);
        placeholder.add(dimYField).width(400).padRight(16f);
        placeholder.add(applyButton).growX();
        return placeholder;

    }

    private LevelData createLevelData() {
        LevelData levelData = new LevelData();

        levelData.setCellArray(hexMapActor.getCellArray());

        levelData.setLevelCode("editor");
        levelData.setLevelNumber(0);

        levelData.setMaxScore(Integer.valueOf(maxScoreField.getText()));
        levelData.setMana(Integer.valueOf(manaField.getText()));

        levelData.setName(nameField.getText());

        return levelData;
    }

    private void resolveLoadedLevelData(LevelData data) {
        hexMapActor.setCellArray(data.getCellArray());
        midRowTable.reset();
        midRowTable.add(hexMapActor).center().expand();

        dimXField.setText(String.valueOf(hexMapActor.getCellArray().length));
        dimYField.setText(String.valueOf(hexMapActor.getCellArray()[0].length));

        manaField.setText(String.valueOf(data.getMana()));
        maxScoreField.setText(String.valueOf(data.getMaxScore()));
        nameField.setText(String.valueOf(data.getName()));
    }
}
