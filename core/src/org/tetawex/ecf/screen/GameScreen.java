package org.tetawex.ecf.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.HexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;
import org.tetawex.ecf.util.RandomProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * ...
 */
public class GameScreen extends BaseECFScreen {
    private static final float PAUSE_BUTTON_WIDTH = 1275f;
    private static final float PAUSE_BUTTON_HEIGHT = 252f;
    private static final float PAUSE_BUTTON_PAD = 32f;
    private static final float PAUSE_BUTTON_FONT_SCALE = 1f;
    private static final float MANA_LABEL_FONT_SCALE = 1f;
    private static final float SCORE_LABEL_FONT_SCALE = 1f;
    private static final float ELEMENT_COUNTER_IMAGE_SIZE = 130f;

    private LevelData levelData;

    private TextureRegion starRegion;
    private TextureRegion starDisabledRegion;
    private Sound winSound;
    private Sound lossSound;

    private Stage gameStage;
    private HexMapActor hexMapActor;
    private Label scoreLabel;
    private TextButton manaLabel;

    private Label fireCounterLabel;
    private Label waterCounterLabel;

    private Label airCounterLabel;
    private Label earthCounterLabel;

    private Label shadowCounterLabel;
    private Label lightCounterLabel;

    private GameData gameData;
    private Label wlLabel;
    private Label wlScore;
    private Label wlSpareMana;
    private Label wlTotal;
    private Label wlReasonLabel;
    private TextButton winLossMenuButtonNext;

    Table pauseTable;

    private ECFPreferences preferences;

    private Map<GameData.LossCondition, String> lcToStringMap;
    private Image[] stars;
    private String levelCode;

    public GameScreen(ECFGame game, Bundle bundle) {
        super(game);
        winSound = getGame().getAssetManager().get("sounds/win.ogg", Sound.class);
        lossSound = getGame().getAssetManager().get("sounds/loss.ogg", Sound.class);

        starRegion = getGame().getTextureRegionFromAtlas("star");
        starDisabledRegion = getGame().getTextureRegionFromAtlas("star_ungained");

        Camera camera = new OrthographicCamera(1440f, 2560f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        gameStage = new Stage(new ExtendViewport(1440f, 2560f, camera));

        Gdx.input.setInputProcessor(gameStage);

        gameData = new GameData();
        levelCode=bundle.getItem("levelCode", String.class,"");

        initUi();
        if (bundle != null) {
            levelData = bundle.getItem("levelData", LevelData.class);
            gameData.setCellArray(levelData.getCellArray());
            gameData.setMana(levelData.getMana());
            gameData.setScore(0);
        } else {
            gameData.setCellArray(CellArrayFactory.generateBasicCellArray(4, 5));
            gameData.setMana(2);
            gameData.setScore(0);
        }

        preferences = PreferencesProvider.getPreferences();

        lcToStringMap = new HashMap<GameData.LossCondition, String>();
        lcToStringMap.put(GameData.LossCondition.NO_MANA, "lc_no_mana");

        lcToStringMap.put(GameData.LossCondition.NO_FIRE, "lc_no_fire");
        lcToStringMap.put(GameData.LossCondition.NO_WATER, "lc_no_water");

        lcToStringMap.put(GameData.LossCondition.NO_AIR, "lc_no_air");
        lcToStringMap.put(GameData.LossCondition.NO_EARTH, "lc_no_earth");

        lcToStringMap.put(GameData.LossCondition.NO_SHADOW, "lc_no_shadow");
        lcToStringMap.put(GameData.LossCondition.NO_LIGHT, "lc_no_light");
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

        final Image background = new Image(getGame().getAssetManager().get("backgrounds/background.png", Texture.class));
        background.setFillParent(true);
        final Image backgroundPause = new Image(getGame().getAssetManager().get("backgrounds/background_pause.png", Texture.class));
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

        hexMapActor = new HexMapActor(getGame());
        hexMapActor.setSoundVolume(PreferencesProvider.getPreferences().getSoundVolume());
        hexMapActor.setCellActionListener(new HexMapActor.CellActionListener() {
            @Override
            public void cellMerged(int mergedElementsCount) {
                gameData.processElementsMerge(mergedElementsCount);
            }

            @Override
            public void cellMoved(int cellElementCount) {
                gameData.spendManaOnMove(cellElementCount);
            }

            @Override
            public boolean canMove(int cellElementCount) {
                return gameData.canMove(cellElementCount);
            }
        });
        midRowTable.add(hexMapActor).center().expand();

        mainTable.setFillParent(true);
        mainTable.add(topRowTable).growX().row();
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

        scoreLabel = new Label("", SkinFactory.generateStandardLabelSkin(getGame()));
        scoreLabel.setFontScale(SCORE_LABEL_FONT_SCALE);
        topRowCenterTable.add(scoreLabel);

        Stack topRowRightStack = new Stack();
        manaLabel = new TextButton("", SkinFactory.generateManaButtonSkin(getGame()));
        topRowRightTable.add(manaLabel).size(150f).pad(40f);

        topRowTable.toFront();
        topRowRightTable.right();
        topRowLeftTable.left();
        final Table wlTable = new Table();

        gameData.setGameDataChangedListener(new GameData.GameDataChangedListener() {
            @Override
            public void manaChanged(int newValue) {
                manaLabel.setText(newValue + "");
            }

            @Override
            public void scoreChanged(int newValue) {
                scoreLabel.setText(newValue + "");
            }

            @Override
            public void cellMapChanged(Cell[][] newMap) {
                hexMapActor.setCellArray(newMap);
            }

            @Override
            public void elementsCountChanged(int fire, int water, int air, int earth, int shadow, int light) {
                fireCounterLabel.setText(fire + "");
                waterCounterLabel.setText(water + "");
                airCounterLabel.setText(air + "");
                earthCounterLabel.setText(earth + "");
                shadowCounterLabel.setText(shadow + "");
                lightCounterLabel.setText(light + "");
            }

            @Override
            public void gameLostOrWon(boolean won, GameData.LossCondition lossCondition) {
                int totalScore = (int) (gameData.getScore() + gameData.getMana() * 100);
                int frequency = 5;
                backgroundPause.setVisible(true);
                winLossMenuButtonNext.setVisible(won);
                wlTable.setVisible(true);
                wlScore.setText(" " + gameData.getScore());
                wlSpareMana.setText(" " + (int) (gameData.getMana() * 100));
                wlTotal.setText(" " + totalScore);
                if (won) {
                    winSound.play(preferences.getSoundVolume());

                    if (levelData.getLevelNumber() == -1) {
                        int i = 0;
                        java.util.List<Score> list = preferences.getScores();
                        for (Score s : list) {
                            if (totalScore > s.getValue()) {
                                break;
                            }
                            i++;
                        }
                        if (i < 12)
                            preferences.getScores().add(i, new Score(totalScore, "Player", levelData.getName()));
                    }
                    winLossMenuButtonNext.setHeight(PAUSE_BUTTON_HEIGHT);
                    wlReasonLabel.setVisible(false);
                    wlLabel.setText(getGame().getLocalisedString("level_success"));
                    int starsCount = Math.round(1 + 2 * (totalScore / levelData.getMaxScore()));
                    if (starsCount > 3)
                        starsCount = 3;
                    for (int i = 0; i < starsCount; i++) {
                        stars[i].setDrawable(new TextureRegionDrawable(starRegion));
                    }
                    if (levelData.getLevelNumber() != -1) {
                        preferences.getLevelCompletionStateList(levelCode).get(levelData.getLevelNumber()).setCompleted(true);
                        if (preferences.getLevelCompletionStateList(levelCode).get(levelData.getLevelNumber()).getStars() < starsCount)
                            preferences.getLevelCompletionStateList(levelCode).get(levelData.getLevelNumber()).setStars(starsCount);
                        if (levelData.getLevelNumber() + 1 < PreferencesProvider.LEVELS_COUNT)
                            preferences.getLevelCompletionStateList(levelCode).get(levelData.getLevelNumber() + 1).setUnlocked(true);
                    }
                    PreferencesProvider.flushPreferences();
                } else {
                    frequency = 10;
                    lossSound.play(preferences.getSoundVolume());
                    winLossMenuButtonNext.setHeight(0);
                    wlLabel.setText(getGame().getLocalisedString("level_fail"));
                    wlReasonLabel.setText(getGame().getLocalisedString(lcToStringMap.get(lossCondition)));
                    wlReasonLabel.setVisible(true);
                }
            }
        });
        //Element counter
        Label.LabelStyle elementLabelStyle = SkinFactory.generateDarkestLabelSkin(getGame());
        fireCounterLabel = new Label("0", elementLabelStyle);
        waterCounterLabel = new Label("0", elementLabelStyle);
        airCounterLabel = new Label("0", elementLabelStyle);
        earthCounterLabel = new Label("0", elementLabelStyle);
        shadowCounterLabel = new Label("0", elementLabelStyle);
        lightCounterLabel = new Label("0", elementLabelStyle);

        //fire-water
        Table fwTable = new Table();
        Table fireTable = new Table();
        Table waterTable = new Table();

        fireTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_fire"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        fireTable.add(fireCounterLabel);
        waterTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_water"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        waterTable.add(waterCounterLabel);

        fwTable.add(fireTable);
        fwTable.add(waterTable);

        //air-earth
        Table aeTable = new Table();
        Table airTable = new Table();
        Table earthTable = new Table();

        airTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_air"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        airTable.add(airCounterLabel);
        earthTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_earth"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        earthTable.add(earthCounterLabel);

        aeTable.add(airTable);
        aeTable.add(earthTable);

        //shadow-light
        Table slTable = new Table();
        Table shadowTable = new Table();
        Table lightTable = new Table();

        shadowTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_shadow"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        shadowTable.add(shadowCounterLabel);
        lightTable.add(new Image(
                getGame().getTextureRegionFromAtlas("element_light"))).size(ELEMENT_COUNTER_IMAGE_SIZE).row();
        lightTable.add(lightCounterLabel);

        slTable.add(shadowTable);
        slTable.add(lightTable);

        bottomRowTable.add(fwTable).pad(40f);
        bottomRowTable.add(aeTable).prefWidth(1500f);
        bottomRowTable.add(slTable).pad(40f);


        //win/loss ui
        wlTable.setVisible(false);
        wlLabel = new Label("", SkinFactory.generateLargeStandardLabelSkin(getGame()));
        wlReasonLabel = new Label("", SkinFactory.generateStandardLabelSkin(getGame()));
        wlReasonLabel.setWrap(true);
        wlReasonLabel.setAlignment(Align.center);
        wlTable.add(wlLabel).pad(20f).row();
        wlTable.add(wlReasonLabel).pad(20f).width(PAUSE_BUTTON_WIDTH).row();

        Table starsTable = new Table();
        stars = new Image[3];

        for (int i = 0; i < stars.length; i++) {
            Image image = new Image(starDisabledRegion);
            stars[i] = image;
            starsTable.add(image).size(200).pad(20);
        }

        Table scoreTable = new Table();
        scoreTable.add(new Label(getGame().getLocalisedString("score"), SkinFactory.generateStandardLabelSkin(getGame())));
        wlScore = new Label("", SkinFactory.generateStandardLabelSkin(getGame()));
        scoreTable.add(wlScore).padRight(40f);
        wlTable.add(scoreTable).pad(20f).row();

        Table spareTable = new Table();
        spareTable.add(new Label(getGame().getLocalisedString("spare_mana"), SkinFactory.generateStandardLabelSkin(getGame())));
        wlSpareMana = new Label("", SkinFactory.generateStandardLabelSkin(getGame()));
        spareTable.add(wlSpareMana);
        wlTable.add(spareTable).pad(20f).row();

        Table totalTable = new Table();
        totalTable.add(new Label(getGame().getLocalisedString("total"), SkinFactory.generateStandardLabelSkin(getGame())));
        wlTotal = new Label("", SkinFactory.generateStandardLabelSkin(getGame()));
        totalTable.add(wlTotal);
        wlTable.add(totalTable).pad(20f).row();
        wlTable.add(starsTable).pad(20f).row();

        stack.add(wlTable);

        winLossMenuButtonNext =
                new TextButton(getGame().getLocalisedString("next"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (levelData.getLevelNumber() == -1)
                    getGame().getGameStateManager().setState(GameStateManager.GameState.HIGHSCORES, null);
                else if (levelData.getLevelNumber() + 1 >= PreferencesProvider.LEVELS_COUNT)
                    getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
                else {
                    Bundle bundle = new Bundle();
                    bundle.putItem("levelData", LevelFactory.generateLevel(levelData.getLevelNumber() + 1,levelCode));
                    getGame().getGameStateManager().setState(GameStateManager.GameState.GAME, bundle);
                }
            }
        });
        wlTable.add(winLossMenuButtonNext)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton winLossMenuButtonRetry =
                new TextButton(getGame().getLocalisedString("retry"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wlTable.setVisible(false);
                backgroundPause.setVisible(false);
                gameData.setCellArray(gameData.getOriginalCellArray());
                gameData.setScore(0);
                gameData.setMana(levelData.getMana());
            }
        });
        wlTable.add(winLossMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();


        TextButton winLossMenuButtonQuit =
                new TextButton(getGame().getLocalisedString("quit"), SkinFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
            }
        });
        wlTable.add(winLossMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

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
                gameData.setCellArray(gameData.getOriginalCellArray());
                gameData.setScore(0);
                gameData.setMana(levelData.getMana());
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
                if (levelData.getLevelNumber() != -1)
                    getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
                else
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
    public void onBackPressed(){
        if(pauseTable.isVisible())
            pauseTable.setVisible(false);
        else {
            if (levelData.getLevelNumber() != -1)
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
            else
                getGame().getGameStateManager().setState(GameStateManager.GameState.MODE_SELECT, null);
        }
    }
}
