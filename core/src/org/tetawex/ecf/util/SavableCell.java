package org.tetawex.ecf.util;

import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tetawex on 16.08.17.
 */
public class SavableCell {
    private IntVector2 position;
    private List<Element> elements;
    private boolean noCell;

    public SavableCell(Cell cell) {
        if (cell != null) {
            elements = Arrays.asList(cell.getElements().orderedItems().items);
            position = cell.getPosition();
        } else noCell = true;
    }

    public Cell createCell() {
        if (noCell) {
            return null;
        }
        Cell cell = new Cell(position);
        for (Element e : elements) {
            if (e != null)
                cell.getElements().add(e);
        }
        return cell;
    }
}
