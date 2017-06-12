package org.tetawex.ecf.actor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.Element;
import org.tetawex.ecf.util.IntVector2;
import org.tetawex.ecf.util.MathUtils;

/**
 * Created by tetawex on 02.05.17.
 */
public class HexMapActor extends BaseWidget<ECFGame> {
    public interface CellActionListener {
        void cellMerged(int mergedElementsCount);
        void cellMoved(int cellElementCount);
        boolean canMove(int cellElementCount);
    }
    private Cell[][] cellArray;

    private CellActionListener cellActionListener;

    private float soundVolume=1f;
    private float scalingFactor=1f;

    private float hexagonWidth;
    private float hexagonHeight;

    private float elementWidth=130f;
    private float elementHeight=130f;

    private Cell selectedCell;

    private TextureRegion cellRegion;
    private TextureRegion selectedRegion;
    private TextureRegion adjacentRegion;

    private Sound clickSound;
    private Sound errorSound;
    private Sound mergeSound;

    private OrderedMap<Element,TextureRegion> textureToElementMap;

    public HexMapActor(ECFGame game) {
        super(game);

        cellRegion=getGame().getTextureRegionFromAtlas("hexagon");
        selectedRegion =getGame().getTextureRegionFromAtlas("hexagon_selected");
        adjacentRegion =getGame().getTextureRegionFromAtlas("hexagon_adjacent");

        clickSound =getGame().getAssetManager().get("sounds/click.ogg",Sound.class);
        errorSound =getGame().getAssetManager().get("sounds/error.ogg",Sound.class);
        mergeSound =getGame().getAssetManager().get("sounds/merge.ogg",Sound.class);

        textureToElementMap=new OrderedMap<Element, TextureRegion>();
        textureToElementMap.put(Element.FIRE,getGame()
                .getTextureRegionFromAtlas("element_fire"));
        textureToElementMap.put(Element.WATER,getGame()
                .getTextureRegionFromAtlas("element_water"));
        textureToElementMap.put(Element.AIR,getGame()
                .getTextureRegionFromAtlas("element_air"));
        textureToElementMap.put(Element.EARTH,getGame()
                .getTextureRegionFromAtlas("element_earth"));
        textureToElementMap.put(Element.SHADOW,getGame()
                .getTextureRegionFromAtlas("element_shadow"));
        textureToElementMap.put(Element.LIGHT,getGame()
                .getTextureRegionFromAtlas("element_light"));

        hexagonHeight=350f;
        hexagonWidth=hexagonHeight* MathUtils.getHexagonWidthToHeightRatio();

        cellArray=new Cell[0][0];

        setWidth(hexagonWidth);
        setHeight(hexagonHeight);

        setTouchable(Touchable.enabled);
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
                processIndexClick(getCellIndexByVector(new Vector2(x,y)));
                return true;
            }
        });
    }
    @Override
    public void act(float deltaTime) {
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        //draw hexagons
        for(int i = 0; i <cellArray.length ; i++) {
            for (int j = 0; j <cellArray[i].length ; j++) {
                if(cellExistsAt(i,j)) {
                    IntVector2 vec = cellArray[i][j].getPosition();
                    Vector2 offset = findOffsetForIndex(vec);
                    batch.draw(cellRegion, getX() + vec.x * (hexagonWidth + offset.x), getY() + vec.y * hexagonHeight + offset.y,
                            hexagonWidth, hexagonHeight);
                }
            }
        }
        //draw selected cells
        if(selectedCell!=null) {
            IntVector2 vec = selectedCell.getPosition();
            Vector2 offset = findOffsetForIndex(vec);
            batch.draw(selectedRegion, getX() + vec.x * (hexagonWidth + offset.x), getY() + vec.y * hexagonHeight + offset.y,
                    hexagonWidth, hexagonHeight);
            for (Cell cell:getAdjacentCells(vec)) {
                IntVector2 vec2 = cell.getPosition();
                Vector2 offset2 = findOffsetForIndex(vec2);
                batch.draw(adjacentRegion, getX() + vec2.x * (hexagonWidth + offset2.x), getY() + vec2.y * hexagonHeight + offset2.y,
                        hexagonWidth, hexagonHeight);
            }

        }
        //draw elements
        for(int i = 0; i <cellArray.length ; i++) {
            for (int j = 0; j <cellArray[i].length ; j++) {
                if(cellExistsAt(i,j)) {
                    IntVector2 vec = cellArray[i][j].getPosition();
                    Vector2 offset = findOffsetForIndex(vec);
                    Cell cell=cellArray[i][j];
                    Array<Element> elements = cell.getElements().orderedItems();
                    int size=cell.getElements().size;
                    if(size==0)
                        continue;
                    else if(size==1)
                        batch.draw(textureToElementMap.get(elements.get(0)), getX()
                                        + i * (hexagonWidth + offset.x)+hexagonWidth/2
                                        -elementWidth/2,
                                getY() + j * hexagonHeight + offset.y+hexagonHeight/2-elementHeight/2,
                                elementWidth, elementHeight);
                    else if(size==2) {
                        batch.draw(textureToElementMap.get(elements.get(0)), getX()
                                        + i * (hexagonWidth + offset.x) + hexagonWidth*0.33f
                                        - elementWidth / 2,
                                getY() + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight);
                        batch.draw(textureToElementMap.get(elements.get(1)), getX()
                                        + i * (hexagonWidth + offset.x) + hexagonWidth *0.66f
                                        - elementWidth / 2,
                                getY() + j * hexagonHeight + offset.y + hexagonHeight / 2 - elementHeight / 2,
                                elementWidth, elementHeight);
                    }
                    else if(size==3) {
                        batch.draw(textureToElementMap.get(elements.get(0)), getX()
                                        + i * (hexagonWidth + offset.x)+hexagonWidth/2
                                        -elementWidth/2,
                                getY() + j * hexagonHeight + offset.y+hexagonHeight*0.66f
                                        +hexagonHeight*0.050f-elementHeight/2,
                                elementWidth, elementHeight);
                        batch.draw(textureToElementMap.get(elements.get(1)), getX()
                                        + i * (hexagonWidth + offset.x) + hexagonWidth*0.33f
                                        - elementWidth / 2,
                                getY() + j * hexagonHeight + offset.y + hexagonHeight*0.33f
                                        +hexagonHeight*0.050f - elementHeight / 2,
                                elementWidth, elementHeight);
                        batch.draw(textureToElementMap.get(elements.get(2)), getX()
                                        + i * (hexagonWidth + offset.x) + hexagonWidth *0.66f
                                        - elementWidth / 2,
                                getY() + j * hexagonHeight + offset.y + hexagonHeight*0.33f
                                        +hexagonHeight*0.050f - elementHeight / 2,
                                elementWidth, elementHeight);
                    }
                    else
                        batch.draw(textureToElementMap.get(Element.EARTH), getX()
                                        + i * (hexagonWidth + offset.x)+hexagonWidth/2
                                        -elementWidth/2,
                                getY() + j * hexagonHeight + offset.y+hexagonHeight/2-3/2,
                                3, 3);
                }
            }
        }
    }
    private IntVector2 getCellIndexByVector(Vector2 vector){
        int i=(int)((vector.x-hexagonWidth*0.125f) /(hexagonWidth*0.75f));
        int j=(i%2==0)?
                (int)(vector.y /hexagonHeight):
                (int)(((vector.y+hexagonHeight*0.5f) /hexagonHeight)-1);
        return new IntVector2(i,j);
    }
    private Cell getCellByIndex(IntVector2 vector){
        return cellArray[vector.x][vector.y];
    }
    public boolean isAdjacent(IntVector2 pos1,IntVector2 pos2){
        if(pos1.x%2!=0) {
            if(pos1.x-1==pos2.x&&pos1.y+1==pos2.y)
                return  true;
            if(pos1.x+1==pos2.x&&pos1.y+1==pos2.y)
                return  true;
        }
        else {
            if(pos1.x-1==pos2.x&&pos1.y-1==pos2.y)
                return  true;
            if(pos1.x+1==pos2.x&&pos1.y-1==pos2.y)
                return  true;
        }
            if(pos1.x-1==pos2.x&&pos1.y==pos2.y)
                return  true;
            if(pos1.x+1==pos2.x&&pos1.y==pos2.y)
                return  true;
            if(pos1.x==pos2.x&&pos1.y+1==pos2.y)
                return  true;
            if(pos1.x==pos2.x&&pos1.y-1==pos2.y)
                return  true;
        return false;
    }
    private void processIndexClick(IntVector2 position){
        if(!cellExistsAt(position))
            return;
        Cell cell=getCellByIndex(position);
        if(selectedCell==null) {
            selectedCell = cell;
            if(selectedCell.getElements().size==0) {
                selectedCell=null;
                errorSound.play(soundVolume);
                return;
            }
            clickSound.play(soundVolume);
        }
        else {
            if(cell!=null) {
                if(cell!=selectedCell) {
                    if(isAdjacent(selectedCell.getPosition(),cell.getPosition())){
                        if(cellActionListener.canMove(selectedCell.getElements().size)) {

                            cellActionListener.cellMoved(selectedCell.getElements().size);
                            cellActionListener.cellMerged(cell.interactWith(selectedCell));

                            selectedCell = null;
                            mergeSound.play(soundVolume);
                        }
                        else {
                            errorSound.play(soundVolume);
                            selectedCell=null;
                        }
                    }
                    else {
                        if(selectedCell!=null&&cell.getElements().size>0) {
                            selectedCell = cell;
                            clickSound.play(soundVolume);
                        }
                        else {
                            selectedCell = null;
                            errorSound.play(soundVolume);
                        }
                    }


                }
                else {
                    selectedCell = null;
                }

            }
        }
    }
    private Array<Cell> getAdjacentCells(IntVector2 position){
        Array<Cell> adjCells=new Array<Cell>();
        int i=position.x;
        int j=position.y;
        if(cellExistsAt(i-1,j))
            adjCells.add(cellArray[i-1][j]);
        if(cellExistsAt(i+1,j))
            adjCells.add(cellArray[i+1][j]);
        if(cellExistsAt(i,j+1))
            adjCells.add(cellArray[i][j+1]);
        if(cellExistsAt(i,j-1))
            adjCells.add(cellArray[i][j-1]);

        if(i%2!=0) {
            if (cellExistsAt(i - 1, j + 1))
                adjCells.add(cellArray[i - 1][j + 1]);
            if (cellExistsAt(i + 1, j + 1))
                adjCells.add(cellArray[i + 1][j + 1]);
        }
        else{
            if (cellExistsAt(i - 1, j - 1))
                adjCells.add(cellArray[i - 1][j - 1]);
            if (cellExistsAt(i + 1, j - 1))
                adjCells.add(cellArray[i + 1][j - 1]);
        }

        return adjCells;
    }
    private Vector2 findOffsetForIndex(IntVector2 idx){
        return new Vector2(-hexagonWidth / 4,
        idx.x % 2 == 0 ? 0 : hexagonHeight/2);
    }
    private boolean cellExistsAt(int i,int j) {
        return cellExistsAt(new IntVector2(i,j));
    }
    private boolean cellExistsAt(IntVector2 position) {

        if (position.x < 0 || position.y < 0 || position.x >= cellArray.length || position.y >= cellArray[0].length)
            return false;
        return getCellByIndex(position) != null;
    }
    public float getScalingFactor() {
        return scalingFactor;
    }
    @Override
    public float getPrefWidth() {
        return cellArray.length*hexagonWidth*0.75f+hexagonWidth/4;
    }
    @Override
    public float getPrefHeight() {
        return cellArray[0].length*hexagonHeight+hexagonHeight/2;
    }
    /*@Override
    public void setWidth(float width){
        super.setWidth(width);
        hexagonWidth=width/cellArray.length;
    }
    @Override
    public void setHeight(float height){
        super.setWidth(height);
        hexagonHeight=height/cellArray[0].length;
    }*/
    public void setScalingFactor(float scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public Cell[][] getCellArray() {
        return cellArray;
    }

    public void setCellArray(Cell[][] cellArray) {
        this.cellArray = cellArray;
    }

    public CellActionListener getCellActionListener() {
        return cellActionListener;
    }

    public void setCellActionListener(CellActionListener cellActionListener) {
        this.cellActionListener = cellActionListener;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }
}
