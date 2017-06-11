package org.tetawex.ecf.model;

import com.badlogic.gdx.utils.OrderedSet;
import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 19.05.2017.
 */
public class GameData {
    public interface GameDataChangedListener{
        void manaChanged(int newValue);
        void scoreChanged(int newValue);
        void cellMapChanged(Cell[][] newMap);
        void elementsCountChanged(int fire,int water,int air,int earth,int shadow,int light);
        void gameLostOrWon(boolean won);
    }
    private GameDataChangedListener gameDataChangedListener;

    private static final int SINGLE_MERGE_BONUS_SCORE=100;
    private static final int SINGLE_MERGE_BONUS_MANA=2;
    private static final int SINGLE_MOVE_MANACOST=1;

    private int fire;
    private int water;
    private int air;
    private int earth;
    private int shadow;
    private int light;

    private int mana;
    private int score;
    private Cell[][] cellArray;
    private Cell[][] originalCellArray;

    public float getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
        if(mana<0)
            mana=0;
        gameDataChangedListener.manaChanged(mana);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if(score<0)
            score=0;
        gameDataChangedListener.scoreChanged(score);
    }

    public Cell[][] getCellArray() {
        return cellArray;
    }

    public void setCellArray(Cell[][] cellArray) {
        this.cellArray = cellArray;

        int width=cellArray.length;
        int height=0;
        if (width>0)
            height=cellArray[0].length;

        Cell[][] arr=new Cell[width][height];

        for (int i = 0; i <width; i++) {
            for (int j = 0; j <height; j++) {
                if(cellArray[i][j]!=null)
                    arr[i][j]= new Cell(new IntVector2(i,j)
                        ,copyElementSet(cellArray[i][j].getElements()));
                else originalCellArray=null;
            }
        }
        originalCellArray=arr;
        gameDataChangedListener.cellMapChanged(cellArray);

        calculateElementsLeft();
    }
    private OrderedSet<Element> copyElementSet(OrderedSet<Element> oldSet){
        OrderedSet<Element> set=new OrderedSet<Element>();
        set.addAll(oldSet);
        return set;
    }
    public void spendManaOnMove(int movedElementsCount){
        setMana(mana-SINGLE_MOVE_MANACOST);
    }

    public void processElementsMerge(int mergedElementsCount){
        setMana(mana+SINGLE_MERGE_BONUS_MANA*mergedElementsCount);
        setScore(score+=getTotalMergeScore(mergedElementsCount));
        calculateElementsLeft();
        checkIfLostOrWon();
    }
    private int getTotalMergeScore(int mergedElementsCount){
        if(mergedElementsCount>2)
            return 20*SINGLE_MERGE_BONUS_SCORE;
        if(mergedElementsCount>1)
            return 5*SINGLE_MERGE_BONUS_SCORE;
        if(mergedElementsCount>0)
            return SINGLE_MERGE_BONUS_SCORE;
        return 0;
    }
    private int getTotalMergeMana(int mergedElementsCount){
        return SINGLE_MERGE_BONUS_MANA*mergedElementsCount;
    }

    public boolean canMove(int cellElementCount) {
        return mana>=SINGLE_MOVE_MANACOST;
    }

    public Cell[][] getOriginalCellArray() {
        return originalCellArray;
    }

    public GameDataChangedListener getGameDataChangedListener() {
        return gameDataChangedListener;
    }

    public void setGameDataChangedListener(GameDataChangedListener gameDataChangedListener) {
        this.gameDataChangedListener = gameDataChangedListener;
    }
    private void calculateElementsLeft(){
        fire=0;
        water=0;
        earth=0;
        air=0;
        shadow=0;
        light=0;
        for (int i = 0; i < cellArray.length; i++) {
            for (int j = 0; j < cellArray[i].length; j++) {
                if(cellArray[i][j]!=null) {
                    if (cellArray[i][j].getElements().contains(Element.FIRE))
                        fire++;
                    if (cellArray[i][j].getElements().contains(Element.WATER))
                        water++;
                    if (cellArray[i][j].getElements().contains(Element.EARTH))
                        earth++;
                    if (cellArray[i][j].getElements().contains(Element.AIR))
                        air++;
                    if (cellArray[i][j].getElements().contains(Element.SHADOW))
                        shadow++;
                    if (cellArray[i][j].getElements().contains(Element.LIGHT))
                        light++;
                }

            }
        }
        triggerElementsCountChangedEvent();
    }
    private void checkIfLostOrWon(){
        if(fire==0&&water==0&&earth==0&&air==0&&shadow==0&&light==0)
            gameDataChangedListener.gameLostOrWon(true);
        else{
            if(mana<=0)
                gameDataChangedListener.gameLostOrWon(false);
            else if(fire==0&&water!=0||fire!=0&&water==0||
                        air==0&&earth!=0||earth==0&&air!=0||
                        shadow==0&&light!=0||light==0&&shadow!=0){
                    gameDataChangedListener.gameLostOrWon(false);
            }
        }

    }
    private void triggerElementsCountChangedEvent(){
        gameDataChangedListener.elementsCountChanged(fire,water,air,earth,shadow,light);
    }

    public int getFire() {
        return fire;
    }

    public void setFire(int fire) {
        this.fire = fire;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getAir() {
        return air;
    }

    public void setAir(int air) {
        this.air = air;
    }

    public int getEarth() {
        return earth;
    }

    public void setEarth(int earth) {
        this.earth = earth;
    }

    public int getShadow() {
        return shadow;
    }

    public void setShadow(int shadow) {
        this.shadow = shadow;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }
}
