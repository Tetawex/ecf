package org.tetawex.ecf.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 02.05.17.
 */
//TODO: Extract interface!!!
public class Cell {
    private IntVector2 position;
    private OrderedSet<Element> elements;

    public Cell(IntVector2 position, Element... elements) {
        this.elements = new OrderedSet<Element>();
        this.elements.addAll(elements);
        this.position = position;
    }

    public Cell(IntVector2 position, OrderedSet<Element> elements) {
        this.position = position;
        this.elements = elements;
    }

    public Cell(IntVector2 position) {
        this(position, new OrderedSet<Element>());
    }

    public OrderedSet<Element> getElements() {
        return elements;
    }

    public void setElements(OrderedSet<Element> elements) {
        this.elements = elements;
    }

    public void setElements(Element... elementArray) {
        elements.addAll(elementArray);
    }

    public void clear() {
        elements.clear();
    }

    public int interactWith(Cell cell) {
        Array<Element> array = new Array<Element>();

        array.addAll(elements.orderedItems());
        array.addAll(cell.getElements().orderedItems());
        cell.clear();

        return ElementFunctions.advancedResolveElementSet(array, elements);
    }

    public IntVector2 getPosition() {
        return position;
    }

    public void setPosition(IntVector2 position) {
        this.position = position;
    }
}
