package org.tetawex.ecf.model;

import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.ElementFunctions;
import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 17.05.2017.
 */
public class CellFactory {
    public static Cell generateRandomNonEmptyCell(IntVector2 position){
        return new Cell(position,ElementFunctions.generateRandomNonEmptyElementSet());
    }
    public static Cell generateRandomCell(IntVector2 position){
        return new Cell(position,ElementFunctions.generateRandomElementSet());
    }
    public static Cell generateEmptyCell(IntVector2 position){
        return new Cell(position);
    }
}
