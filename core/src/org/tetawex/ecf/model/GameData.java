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
    }
    private GameDataChangedListener gameDataChangedListener;

    private static final int SINGLE_MERGE_BONUS_SCORE=100;
    private static final int SINGLE_MERGE_BONUS_MANA=2;
    private static final int SINGLE_MOVE_MANACOST=1;

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
}
