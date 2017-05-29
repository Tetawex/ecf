package org.tetawex.ecf.model;

import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 23.05.2017.
 */
public class CellArrayFactory {
    public static Cell[][] generateBasicCellArray(int width,int height){
        Cell[][] cellArray=new Cell[width][height];
        for (int i = 0; i <width; i++) {
            for (int j = 0; j <height; j++) {
                cellArray[i][j]= CellFactory.generateRandomCell(new IntVector2(i,j));
            }
        }
        cellArray[0][0]=null;
        cellArray[1][2]=null;
        cellArray[cellArray.length-1][0]=null;
        return cellArray;
    }
}
