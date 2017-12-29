package org.tetawex.ecf.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.tetawex.ecf.actor.HexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.screen.BaseScreen;
import org.tetawex.ecf.screen.StyleFactory;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.IntVector2;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * ...
 */
public class MotTutorialScreen extends BaseScreen<ECFGame> {
    private static final float PAUSE_BUTTON_WIDTH = 1275f;
    private static final float PAUSE_BUTTON_HEIGHT = 252f;
    private static final float PAUSE_BUTTON_PAD = 40f;
    private static final float PAUSE_BUTTON_FONT_SCALE = 1f;
    private static final float MANA_LABEL_FONT_SCALE = 1f;
    private static final float SCORE_LABEL_FONT_SCALE = 1f;

    private Stage gameStage;
    private TutorialHexMapActor hexMapActor;
    private Label scoreLabel;
    private TextButton manaLabel;

    private TextButton tutButton;

    private GameData gameData;

    private int tutorialStage = 0;

    private ECFPreferences preferences;

    public MotTutorialScreen(ECFGame game, Bundle bundle) {
        super(game);
        preferences = PreferencesProvider.getPreferences();
        preferences.setCompletedMotTutorial(true);
        Camera camera = new OrthographicCamera(1440f, 2560f);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
        gameStage = new Stage(new ExtendViewport(1440f, 2560f, camera));

        Gdx.input.setInputProcessor(gameStage);

        gameData = new GameData();

        initUi();

        LevelData levelData = LevelFactory.generateMotTutorial();

        gameData.setCellArray(levelData.getCellArray());
        gameData.setMana(levelData.getMana());

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
        final Table pauseTable = new Table();
        pauseTable.setVisible(false);

        Table mainTable = new Table();
        Stack stack = new Stack();
        stack.setFillParent(true);

        final Image background = new Image(getGame().getAssetManager().get("backgrounds/motbackground.png", Texture.class));
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

        hexMapActor = new TutorialHexMapActor(getGame());
        hexMapActor.setCellActionListener(new HexMapActor.CellActionListener() {
            @Override
            public void cellMerged(int mergedElementsCount) {
                gameData.processElementsMerge(mergedElementsCount);
            }

            @Override
            public void cellMoved(int cellElementCount) {
                gameData.spendManaOnMove(cellElementCount);
                advanceTutorial();
            }

            @Override
            public boolean canMove(int cellElementCount) {
                return gameData.canMove(cellElementCount);
            }
        });
        midRowTable.add(hexMapActor).center().expand().padTop(60f);

        mainTable.setFillParent(true);
        mainTable.add(topRowTable).growX().row();
        mainTable.add(midRowTable).growX().growY().row();
        mainTable.add(bottomRowTable).growX().prefHeight(600f);

        Stack tutStack = new Stack();
        tutButton = new TextButton(getGame().getLocalisedString("mottutorial0"), StyleFactory.generateStandardTutorialButtonStyle(getGame()));
        tutButton.getLabel().setWrap(true);
        bottomRowTable.add(tutButton).pad(PAUSE_BUTTON_PAD).width(PAUSE_BUTTON_WIDTH).grow();

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

        scoreLabel = new Label("", StyleFactory.generateStandardLabelStyle(getGame()));
        scoreLabel.setFontScale(SCORE_LABEL_FONT_SCALE);
        topRowCenterTable.add(scoreLabel);

        Stack topRowRightStack = new Stack();
        manaLabel = new TextButton("", StyleFactory.generateManaButtonStyle(getGame()));
        topRowRightTable.add(manaLabel).size(150f).pad(40f);

        topRowTable.toFront();
        topRowRightTable.right();
        topRowLeftTable.left();

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
            public void elementsCountChanged(int fire, int water, int air, int earth, int shadow, int light, int time) {

            }

            @Override
            public void gameLostOrWon(boolean won, GameData.LossCondition lossCondition) {

            }
        });
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

        TextButton pauseMenuButtonQuit =
                new TextButton(getGame().getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonStyle(getGame()));
        pauseMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                Bundle bundle = new Bundle();
                bundle.putItem("levelCode", "mot");
                getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU, bundle);
            }
        });
        pauseMenuButtonQuit.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH, PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();
    }

    public void advanceTutorial() {
        tutorialStage++;
        if (tutorialStage == 1) {
            hexMapActor.lockCells();
            hexMapActor.unlockedCells[2][0] = true;
            hexMapActor.unlockedCells[2][1] = true;
            hexMapActor.fromCell = new IntVector2(2, 0);
            hexMapActor.toCell = new IntVector2(2, 1);
            hexMapActor.acceptAnyClick = false;
        } else if (tutorialStage == 2) {
            hexMapActor.lockCells();
            hexMapActor.unlockedCells[1][1] = true;
            hexMapActor.unlockedCells[1][0] = true;
            hexMapActor.fromCell = new IntVector2(1, 1);
            hexMapActor.toCell = new IntVector2(1, 0);
            hexMapActor.acceptAnyClick = false;
        } else if (tutorialStage == 3) {
            hexMapActor.lockCells();
            hexMapActor.unlockedCells[1][0] = true;
            hexMapActor.unlockedCells[2][1] = true;
            hexMapActor.fromCell = new IntVector2(2, 1);
            hexMapActor.toCell = new IntVector2(1, 0);
            hexMapActor.acceptAnyClick = false;
        } else if (tutorialStage == 4) {
            hexMapActor.unlockedCells[2][1] = false;
            hexMapActor.acceptAnyClick = true;
        } else if (tutorialStage == 5) {
            hexMapActor.lockCells();
            hexMapActor.unlockedCells[1][0] = true;
            hexMapActor.unlockedCells[0][1] = true;
            hexMapActor.fromCell = new IntVector2(1, 0);
            hexMapActor.toCell = new IntVector2(0, 1);
            hexMapActor.acceptAnyClick = false;

        } else if (tutorialStage == 6) {
            hexMapActor.lockCells();
            hexMapActor.fromCell = new IntVector2(100, 100);
            hexMapActor.toCell = new IntVector2(100, 100);
            hexMapActor.acceptAnyClick = true;
        } else if (tutorialStage >= 8) {
            Bundle bundle = new Bundle();
            bundle.putItem("levelCode", "mot");
            getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, bundle);
            return;
        }
        tutButton.setText(getGame().getLocalisedString("mottutorial" + tutorialStage));
    }
}