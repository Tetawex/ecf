package org.tetawex.ecf.util;

import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.LevelData;

/**
 * Created by tetawex on 16.08.17.
 */
public class SavableLevelData {
    private SavableCell[][] cellArray;
    private int mana;
    private int maxScore;
    private String name;


    public SavableLevelData(LevelData data) {
        mana = data.getMana();
        maxScore = data.getMaxScore();
        name = data.getName();

        int width = data.getCellArray().length;
        int height = data.getCellArray()[0].length;
        cellArray = new SavableCell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cellArray[i][j] = new SavableCell(data.getCellArray()[i][j]);
            }
        }
    }

    public LevelData createLevelData() {
        LevelData levelData = new LevelData();
        levelData.setLevelNumber(0);
        levelData.setMaxScore(maxScore);
        levelData.setLevelCode("editor");
        levelData.setMana(mana);
        levelData.setName(name);

        int width = cellArray.length;
        int height = cellArray[0].length;
        Cell[][] trueCellArray = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                trueCellArray[i][j] = cellArray[i][j].createCell();
            }
        }

        levelData.setCellArray(trueCellArray);
        return levelData;
    }
}
