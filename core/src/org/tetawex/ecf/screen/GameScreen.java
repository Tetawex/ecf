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
import org.tetawex.ecf.actor.HexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.core.GameStateManager;
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.util.Bundle;
import org.tetawex.ecf.util.PreferencesProvider;

/**
 * ...
 */
public class GameScreen extends BaseScreen<ECFGame> {
    private static final float PAUSE_BUTTON_WIDTH =1275f;
    private static final float PAUSE_BUTTON_HEIGHT =252f;
    private static final float PAUSE_BUTTON_PAD=32f;
    private static final float PAUSE_BUTTON_FONT_SCALE=1f;
    private static final float MANA_LABEL_FONT_SCALE =1f;
    private static final float SCORE_LABEL_FONT_SCALE =1f;
    private LevelData data;

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
    private TextButton winLossMenuButtonNext;

    public GameScreen(ECFGame game,Bundle bundle){
        super(game);

        Camera camera=new OrthographicCamera(1440f,2560f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        gameStage=new Stage(new ExtendViewport(1440f,2560f,camera));

        Gdx.input.setInputProcessor(gameStage);

        gameData=new GameData();

        initUi();
        if(bundle!=null){
            data=bundle.getItem("levelData",LevelData.class);
            gameData.setCellArray(data.getCellArray());
            gameData.setMana(data.getMana());
            gameData.setScore(0);
        }
        else {
            gameData.setCellArray(CellArrayFactory.generateBasicCellArray(4, 5));
            gameData.setMana(2);
            gameData.setScore(0);
        }
    }

    @Override
    public void render(float delta){
        gameStage.act();
        gameStage.draw();
    }
    @Override
    public void resize(int width,int height){
        gameStage.getViewport().update(width,height,true);
        gameStage.getViewport().getCamera().update();
    }

    private void initUi(){
        final Table pauseTable=new Table();
        pauseTable.setVisible(false);

        Table mainTable=new Table();
        Stack stack=new Stack();
        stack.setFillParent(true);

        final Image background=new Image(getGame().getAssetManager().get("backgrounds/background.png", Texture.class));
        background.setFillParent(true);
        final Image backgroundPause=new Image(getGame().getAssetManager().get("backgrounds/background_pause.png", Texture.class));
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

        hexMapActor=new HexMapActor(getGame());
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
        midRowTable.add(hexMapActor).center().expand().padTop(60f);

        mainTable.setFillParent(true);
        mainTable.add(topRowTable).growX().row();
        mainTable.add(midRowTable).growX().growY().row();
        mainTable.add(bottomRowTable).growX().prefHeight(300f);

        TextButton pauseButton=new TextButton(" ", StyleFactory.generatePauseButtonSkin(getGame()));
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
            }
        });
        topRowLeftTable.left().top();
        topRowLeftTable.add(pauseButton).size(120f).pad(40f).center();

        scoreLabel =new Label("",StyleFactory.generateStandardLabelSkin(getGame()));
        scoreLabel.setFontScale(SCORE_LABEL_FONT_SCALE);
        topRowCenterTable.add(scoreLabel);

        Stack topRowRightStack=new Stack();
        manaLabel =new TextButton("",StyleFactory.generateManaButtonSkin(getGame()));
        topRowRightTable.add(manaLabel).size(150f).pad(40f);

        topRowTable.toFront();
        topRowRightTable.right();
        topRowLeftTable.left();
        final Table wlTable=new Table();

        gameData.setGameDataChangedListener(new GameData.GameDataChangedListener() {
            @Override
            public void manaChanged(int newValue) {
                manaLabel.setText(newValue+"");
            }

            @Override
            public void scoreChanged(int newValue) {
                scoreLabel.setText(newValue+"");
            }

            @Override
            public void cellMapChanged(Cell[][] newMap) {
                hexMapActor.setCellArray(newMap);
            }

            @Override
            public void gameLostOrWon(boolean won) {
                backgroundPause.setVisible(true);
                winLossMenuButtonNext.setVisible(won);
                wlTable.setVisible(true);
                wlScore.setText(" "+gameData.getScore());
                wlSpareMana.setText(" "+gameData.getMana()*100);
                wlTotal.setText(" "+(gameData.getScore()+gameData.getMana()*100));
                if(won)
                    wlLabel.setText(getGame().getLocalisedString("level_success"));
                else
                    wlLabel.setText(getGame().getLocalisedString("level_fail"));
            }
        });
          //Elemental counter
        /*Label.LabelStyle elementLabelStyle=StyleFactory.generateDarkerLabelSkin(getGame());
        fireCounterLabel=new Label("0",elementLabelStyle);
        waterCounterLabel=new Label("0",elementLabelStyle);
        airCounterLabel=new Label("0",elementLabelStyle);
        earthCounterLabel=new Label("0",elementLabelStyle);
        shadowCounterLabel=new Label("0",elementLabelStyle);
        lightCounterLabel=new Label("0",elementLabelStyle);

        Table fwTable=new Table();
        Table fireTable=new Table();
        Table waterTable=new Table();*/

        //win/loss ui
        wlTable.setVisible(false);
        wlLabel = new Label("",StyleFactory.generateStandardLabelSkin(getGame()));
        wlTable.add(wlLabel).row();

        Table scoreTable=new Table();
        scoreTable.add(new Label(getGame().getLocalisedString("score"),StyleFactory.generateStandardLabelSkin(getGame())));
        wlScore = new Label("",StyleFactory.generateStandardLabelSkin(getGame()));
        scoreTable.add(wlScore).padRight(40f);
        wlTable.add(scoreTable).row();

        Table spareTable=new Table();
        spareTable.add(new Label(getGame().getLocalisedString("spare_mana"),StyleFactory.generateStandardLabelSkin(getGame())));
        wlSpareMana = new Label("",StyleFactory.generateStandardLabelSkin(getGame()));
        spareTable.add(wlSpareMana);
        wlTable.add(spareTable).row();

        Table totalTable=new Table();
        totalTable.add(new Label(getGame().getLocalisedString("total"),StyleFactory.generateStandardLabelSkin(getGame())));
        wlTotal = new Label("",StyleFactory.generateStandardLabelSkin(getGame()));
        totalTable.add(wlTotal);
        wlTable.add(totalTable).row();

        stack.add(wlTable);

        winLossMenuButtonNext=
                new TextButton(getGame().getLocalisedString("next"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonNext.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.LEVEL_SELECT, null);
            }
        });
        wlTable.add(winLossMenuButtonNext)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton winLossMenuButtonRetry=
                new TextButton(getGame().getLocalisedString("retry"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                wlTable.setVisible(false);
                backgroundPause.setVisible(false);
                gameData.setCellArray(gameData.getOriginalCellArray());
                gameData.setScore(0);
                gameData.setMana(data.getMana());
            }
        });
        wlTable.add(winLossMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();


        TextButton winLossMenuButtonQuit=
                new TextButton(getGame().getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        winLossMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU, null);
            }
        });
        wlTable.add(winLossMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        //pause ui
        pauseTable.center();

        TextButton pauseMenuButtonContinue=
                new TextButton(getGame().getLocalisedString("continue"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonContinue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
            }
        });
        pauseMenuButtonContinue.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonContinue)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton pauseMenuButtonRetry=
                new TextButton(getGame().getLocalisedString("retry"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                gameData.setCellArray(gameData.getOriginalCellArray());
                gameData.setScore(0);
                gameData.setMana(data.getMana());
            }
        });
        pauseMenuButtonRetry.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();


        TextButton pauseMenuButtonQuit=
                new TextButton(getGame().getLocalisedString("quit"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
               getGame().getGameStateManager().setState(GameStateManager.GameState.MAIN_MENU,null);
            }
        });
        pauseMenuButtonQuit.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();
    }
}
