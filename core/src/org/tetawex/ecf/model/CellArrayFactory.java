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
                cellArray[i][j]= CellFactory.generateRandomNonEmptyCell(new IntVector2(i,j));
            }
        }
        return cellArray;
    }
    public static Cell[][] generateEmptyCellArray(int width,int height){
        Cell[][] cellArray=new Cell[width][height];
        for (int i = 0; i <width; i++) {
            for (int j = 0; j <height; j++) {
                cellArray[i][j]= CellFactory.generateEmptyCell(new IntVector2(i,j));
            }
        }
        return cellArray;
    }
    public static Cell[][] generateTutorialCellArray(){
        Cell[][] cellArray=new Cell[4][4];
        for (int i = 0; i <4; i++) {
            for (int j = 0; j <4; j++) {
                cellArray[i][j]= CellFactory.generateEmptyCell(new IntVector2(i,j));
            }
        }
        cellArray[0][0]=null;
        cellArray[2][0]=null;
        cellArray[0][1]=null;
        cellArray[1][0]=null;
        cellArray[0][3]=null;
        cellArray[1][3]=null;

        cellArray[0][2].setElements(Element.WATER,Element.AIR,Element.LIGHT);
        cellArray[1][1].setElements(Element.FIRE,Element.EARTH);
        cellArray[1][2].setElements(Element.FIRE,Element.SHADOW);
        cellArray[2][1].setElements(Element.FIRE);
        cellArray[2][2].setElements(Element.AIR);
        cellArray[2][3].setElements(Element.AIR);
        cellArray[3][0].setElements(Element.WATER);
        cellArray[3][1].setElements(Element.EARTH);
        cellArray[3][2].setElements(Element.LIGHT);
        cellArray[3][3].setElements(Element.SHADOW);

        return cellArray;
    }
}
