package org.tetawex.ecf.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.tetawex.ecf.actor.HexMapActor;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.util.IntVector2;

/**
 * Created by tetawex on 21.08.17.
 */
public class TutorialHexMapActor extends HexMapActor {

    public boolean[][] unlockedCells = new boolean[1][1];
    public IntVector2 fromCell = new IntVector2(2, 0);
    public IntVector2 toCell = new IntVector2(2, 1);

    public boolean acceptAnyClick = true;

    private TextureRegion disabledRegion;

    public TutorialHexMapActor(ECFGame game) {
        super(game);

        disabledRegion = getGame().getTextureRegionFromAtlas("hexagon_disabled");

        setTouchable(Touchable.enabled);
        getListeners().clear();
        addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                if (acceptAnyClick)
                    cellActionListener.cellMoved(0);
                processIndexClick(getCellIndexByVector(new Vector2(x, y)));
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[i].length; j++) {
                if (cellExistsAt(i, j) && !unlockedCells[i][j]) {
                    IntVector2 vec = cellArray[i][j].getPosition();
                    Vector2 offset = findOffsetForIndex(vec);
                    batch.draw(disabledRegion, getX() + vec.x * (hexagonWidth + offset.x), getY() + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight);
                }
            }
        }
    }

    @Override
    protected void processIndexClick(IntVector2 position) {
        if (!cellExistsAt(position))
            return;
        Cell cell = getCellByIndex(position);
        if (selectedCell == null) {
            selectedCell = cell;
            if (!(cell.getPosition().x == fromCell.x && cell.getPosition().y == fromCell.y)) {
                selectedCell = null;
                errorSound.play(soundVolume);
                return;
            }
            //Gdx.app.log("cell", selectedCell.getPosition().x+"");
            clickSound.play(soundVolume);
        } else {
            if (cell != null) {
                if (cell != selectedCell) {
                    if (cell.getPosition().x == toCell.x && cell.getPosition().y == toCell.y) {
                        cellActionListener.cellMoved(selectedCell.getElements().size);
                        cellActionListener.cellMerged(cell.interactWith(selectedCell));

                        selectedCell = null;
                        mergeSound.play(soundVolume);
                    }

                } else {
                    selectedCell = null;
                }

            }
        }
        Gdx.app.log("cell", (selectedCell == null) + "");
    }

    @Override
    public void setCellArray(Cell[][] cellArray) {
        this.cellArray = cellArray;
        unlockedCells = new boolean[cellArray.length][cellArray[0].length];
    }

    public boolean[][] getUnlockedCells() {
        return unlockedCells;
    }

    public void setUnlockedCells(boolean[][] unlockedCells) {
        this.unlockedCells = unlockedCells;
    }

    public IntVector2 getFromCell() {
        return fromCell;
    }

    public void setFromCell(IntVector2 fromCell) {
        this.fromCell = fromCell;
    }

    public IntVector2 getToCell() {
        return toCell;
    }

    public void setToCell(IntVector2 toCell) {
        this.toCell = toCell;
    }

    public void lockCells() {
        unlockedCells = new boolean[4][4];
    }

    public void unlockCells() {
        for (int i = 0; i < unlockedCells.length; i++) {
            for (int j = 0; j < unlockedCells[i].length; j++) {
                unlockedCells[i][j] = true;
            }
        }
    }
}
