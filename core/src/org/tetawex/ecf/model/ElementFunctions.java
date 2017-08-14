package org.tetawex.ecf.model;

import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import org.tetawex.ecf.util.RandomProvider;

import java.util.List;

/**
 * Created by Tetawex on 17.05.2017.
 */
public class ElementFunctions {
    public static OrderedSet<Element> generateRandomElementSet() {
        RandomXS128 random = RandomProvider.getRandom();
        OrderedSet<Element> set = new OrderedSet<Element>();
        for (int i = 0; i < Element.getElementsCount(); i++) {
            if (random.nextBoolean()) {
                set.add(Element.getElementById(i + 1));
            }
        }
        resolveElementSet(set);
        return set;
    }

    public static OrderedSet<Element> generateRandomNonEmptyElementSet() {
        RandomXS128 random = RandomProvider.getRandom();
        OrderedSet<Element> set = new OrderedSet<Element>();
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.FIRE);
            else set.add(Element.WATER);
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.EARTH);
            else set.add(Element.AIR);
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean())
                set.add(Element.LIGHT);
            else set.add(Element.SHADOW);
        }
        if (set.size == 0) {
            set.add(Element.getElementById(1 + random.nextInt(Element.getElementsCount() - 1)));
        }
        return set;
    }

    public static int resolveElementSet(OrderedSet<Element> elements) {

        Array<Element> deletionList = new Array<Element>();
        int reactions = 0;
        for (Element element : elements) {
            Element oppositeElement = Element.getOpposite(element);
            if (elements.contains(oppositeElement) && !deletionList.contains(element, true)) {
                deletionList.add(element);
                deletionList.add(oppositeElement);
                reactions++;
            }
        }
        for (Element e : deletionList) {
            elements.remove(e);
        }
        return reactions;
    }

    public static int advancedResolveElementSet(Array<Element> elements, OrderedSet<Element> targetSet) {

        Array<Element> deletionList = new Array<Element>();
        for (Element e : elements) {
            if (e.equals(Element.TIME))
                deletionList.add(e);
        }
        elements.removeAll(deletionList, true);
        targetSet.clear();
        targetSet.addAll(elements);
        if (deletionList.size % 2 == 0) {
            return resolveElementSet(targetSet);
        } else {
            targetSet.add(Element.TIME);
            return 0;
        }
    }
    public static void addElementToCell(Cell cell, Element element) {
        Array<Element> result = new Array<Element>();
        result.addAll(cell.getElements().orderedItems());
        result.add(element);
        advancedResolveElementSet(result, cell.getElements());
    }
}
