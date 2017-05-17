package org.tetawex.ecf.model;

/**
 * Created by tetawex on 02.05.17.
 */
public enum Element {
    FIRE,WATER,LIGHT,SHADOW,EARTH,WIND,NEUTRAL;
    public static Element getOpposite(Element element){
        switch (element){
            case WATER:
                return FIRE;
            case FIRE:
                return WATER;
            case EARTH:
                return WIND;
            case WIND:
                return EARTH;
            case LIGHT:
                return SHADOW;
            case SHADOW:
                return LIGHT;
        }
        return FIRE;
    }
    public static Element getElementById(int id){
        switch(id){
            case 1:
                return FIRE;
            case 2:
                return WATER;
            case 3:
                return WIND;
            case 4:
                return EARTH;
            case 5:
                return SHADOW;
            case 6:
                return LIGHT;
        }
        return NEUTRAL;
    }
}
