package org.tetawex.ecf.model;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class Score {
    private int value;
    private String name;
    private String levelName;

    public Score(int value, String name, String levelName) {
        this.value = value;
        this.name = name;
        this.levelName = levelName;
    }

    public Score() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
