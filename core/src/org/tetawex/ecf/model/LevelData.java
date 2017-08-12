package org.tetawex.ecf.model;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class LevelData {
    private Cell[][] cellArray;
    private int mana;
    private int maxScore;
    private int levelNumber;
    private String levelCode;
    private String name;

    public LevelData() {
    }

    public LevelData(Cell[][] cellArray, int mana, int maxScore, int levelNumber, String name, String levelCode) {
        this.cellArray = cellArray;
        this.mana = mana;
        this.maxScore = maxScore;
        this.levelNumber = levelNumber;
        this.name = name;
        this.levelCode=levelCode;
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }
}
