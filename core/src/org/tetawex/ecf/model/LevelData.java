package org.tetawex.ecf.model;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class LevelData {
    private Cell[][] cellArray;
    private int mana;
    private int maxScore;
    public LevelData(){
    }

    public LevelData(Cell[][] cellArray, int mana, int maxScore) {
        this.cellArray = cellArray;
        this.mana = mana;
        this.maxScore = maxScore;
    }

    public Cell[][] getCellArray() {
        return cellArray;
    }

    public void setCellArray(Cell[][] cellArray) {
        this.cellArray = cellArray;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
