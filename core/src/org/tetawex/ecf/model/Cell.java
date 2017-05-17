package org.tetawex.ecf.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tetawex on 02.05.17.
 */
public class Cell {
    private Set<Element> elements;
    public Cell(Element... elements){
        this.elements=new HashSet<Element>();
        this.elements.addAll(Arrays.asList(elements));
    }
    public Set<Element> getElements(){
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    public void interactWith(Cell cell){
        elements.addAll(cell.getElements());
        cell.setElements(new HashSet<Element>());
        for (Element element:elements) {
            Element oppositeElement=Element.getOpposite(element);
            if(elements.contains(oppositeElement)) {
                elements.remove(element);
                elements.remove(oppositeElement);
            }

        }
    }
}
