package org.tetawex.ecf.model;

/**
 * Created by Tetawex on 02.05.17.
 */
public enum Element {
    FIRE, WATER, LIGHT, SHADOW, EARTH, AIR, TIME;

    public static int getElementsCount() {
        //return values().length;
        return 6;
    }

    public static Element getOpposite(Element element) {
        switch (element) {
            case WATER:
                return FIRE;
            case FIRE:
                return WATER;
            case EARTH:
                return AIR;
            case AIR:
                return EARTH;
            case LIGHT:
                return SHADOW;
            case SHADOW:
                return LIGHT;
        }
        return TIME;
    }

    public static Element getElementById(int id) {
        switch (id) {
            case 1:
                return FIRE;
            case 2:
                return WATER;
            case 3:
                return AIR;
            case 4:
                return EARTH;
            case 5:
                return SHADOW;
            case 6:
                return LIGHT;
        }
        return TIME;
    }
}
