package org.tetawex.ecf.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

    private Stage gameStage;
    private HexMapActor hexMapActor;
    private Label scoreLabel;
    private TextButton manaLabel;

    private GameData gameData;

    public GameScreen(ECFGame game,Bundle bundle){
        super(game);

        Camera camera=new OrthographicCamera(1440f,2560f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        gameStage=new Stage(new ExtendViewport(1440f,2560f,camera));

        Gdx.input.setInputProcessor(gameStage);

        gameData=new GameData();

        initUi();

        gameData.setCellArray(CellArrayFactory.generateBasicCellArray(3,5));
        gameData.setMana(2);
        gameData.setScore(0);
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
        });
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
                gameData.setMana(2);
            }
        });
        pauseMenuButtonRetry.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton pauseMenuButtonSkip=
                new TextButton(getGame().getLocalisedString("next"), StyleFactory.generateStandardMenuButtonSkin(getGame()));
        pauseMenuButtonSkip.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                gameData.setCellArray(CellArrayFactory.generateBasicCellArray(3,5));
                gameData.setScore(0);
                gameData.setMana(2);
            }
        });
        pauseMenuButtonSkip.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonSkip)
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
