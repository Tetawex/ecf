package org.tetawex.ecf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class ECFPreferences {
    private List<Score> scores = new ArrayList<Score>();
    private List<LevelCompletionState> levelCompletionStateList = new ArrayList<LevelCompletionState>();
    private List<LevelCompletionState> levelCompletionStateListMot = new ArrayList<LevelCompletionState>();

    private boolean completedTutorial;

    private float soundVolume = 0.5f;
    private float musicVolume = 0.5f;

    private String selectedLanguageCode;
    private String selectedCountryCode;

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public String getSelectedLanguageCode() {
        return selectedLanguageCode;
    }

    public List<LevelCompletionState> getLevelCompletionStateList(String levelCode) {
        if ("".equals(levelCode))
            return levelCompletionStateList;
        else if ("mot".equals(levelCode))
            return levelCompletionStateListMot;
        return null;
    }

    public void setLevelCompletionStateList(List<LevelCompletionState> list, String levelCode) {
        if ("".equals(levelCode))
            this.levelCompletionStateList = list;
        else if (("mot".equals(levelCode)))
            this.levelCompletionStateListMot = list;
    }

    public String getSelectedCountryCode() {
        return selectedCountryCode;
    }

    public void setSelectedLanguage(String language) {
        String[] str = language.split("_");
        selectedLanguageCode = str[0];
        if (str.length > 1)
            selectedCountryCode = str[1];
        else selectedCountryCode = "";
    }

    public String getSelectedLanguage() {
        return selectedLanguageCode + "_" +
                selectedCountryCode;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public void setSelectedLanguageCode(String selectedLanguageCode) {
        this.selectedLanguageCode = selectedLanguageCode;
    }

    public void setSelectedCountryCode(String selectedCountryCode) {
        this.selectedCountryCode = selectedCountryCode;
    }

    public boolean isCompletedTutorial() {
        return completedTutorial;
    }

    public void setCompletedTutorial(boolean completedTutorial) {
        this.completedTutorial = completedTutorial;
    }
}
