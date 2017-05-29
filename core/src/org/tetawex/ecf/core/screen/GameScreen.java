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
import org.tetawex.ecf.model.*;
import org.tetawex.ecf.model.Cell;

/**
 * ...
 */
public class GameScreen extends BaseScreen<ECFGame> {
    private static final float PAUSE_BUTTON_WIDTH=1400f;
    private static final float PAUSE_BUTTON_HEIGHT=350f;
    private static final float PAUSE_BUTTON_PAD=50f;
    private static final float PAUSE_BUTTON_FONT_SCALE=5f;
    private static final float LABEL_FONT_SCALE=5f;

    private Stage gameStage;
    private HexMapActor hexMapActor;
    private Label scoreLabel;
    private Label manaLabel;

    private GameData gameData;

    public GameScreen(ECFGame game){
        super(game);

        Camera camera=new OrthographicCamera(2560f,1440f);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0f);
        gameStage=new Stage(new ExtendViewport(2560f,1440f,camera));

        Music music=Gdx.audio.newMusic(Gdx.files.internal("music/ambient.ogg"));
        music.setLooping(true);
        music.play();

        Gdx.input.setInputProcessor(gameStage);

        gameData=new GameData();

        initUi();

        gameData.setCellArray(CellArrayFactory.generateBasicCellArray(3,5));
        gameData.setMana(20);
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
        topRowTable.add(topRowLeftTable).width(600f);
        topRowTable.add(topRowCenterTable).expandX();
        topRowTable.add(topRowRightTable).width(600f);;

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
        midRowTable.add(hexMapActor).center().expand();

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
        topRowLeftTable.add(pauseButton).size(300f).pad(50f).center();

        scoreLabel =new Label("25670",StyleFactory.generateScoreLabelSkin(getGame()));
        scoreLabel.setFontScale(LABEL_FONT_SCALE);
        topRowCenterTable.add(scoreLabel);

        manaLabel =new Label("205",StyleFactory.generateManaLabelSkin(getGame()));
        manaLabel.setFontScale(LABEL_FONT_SCALE);
        topRowRightTable.add(manaLabel).padRight(25f);

        topRowRightTable
                .add(new Image(getGame().getTextureRegionFromAtlas("mana")))
                .size(250f).padRight(50f);

        topRowTable.toFront();

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
                new TextButton("CONTINUE", StyleFactory.generatePauseMenuButtonSkin(getGame()));
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
                new TextButton("RETRY", StyleFactory.generatePauseMenuButtonSkin(getGame()));
        pauseMenuButtonRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                gameData.setCellArray(gameData.getOriginalCellArray());
                gameData.setScore(0);
                gameData.setMana(20);
            }
        });
        pauseMenuButtonRetry.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonRetry)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton pauseMenuButtonSkip=
                new TextButton("SKIP", StyleFactory.generatePauseMenuButtonSkin(getGame()));
        pauseMenuButtonSkip.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                gameData.setCellArray(CellArrayFactory.generateBasicCellArray(3,5));
                gameData.setScore(0);
                gameData.setMana(20);
            }
        });
        pauseMenuButtonSkip.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonSkip)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();

        TextButton pauseMenuButtonQuit=
                new TextButton("QUIT", StyleFactory.generatePauseMenuButtonSkin(getGame()));
        pauseMenuButtonQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(!pauseTable.isVisible());
                backgroundPause.setVisible(!backgroundPause.isVisible());
                Gdx.app.exit();
            }
        });
        pauseMenuButtonQuit.getLabel().setFontScale(PAUSE_BUTTON_FONT_SCALE);
        pauseTable.add(pauseMenuButtonQuit)
                .size(PAUSE_BUTTON_WIDTH,PAUSE_BUTTON_HEIGHT)
                .center().pad(PAUSE_BUTTON_PAD).row();
    }
}
