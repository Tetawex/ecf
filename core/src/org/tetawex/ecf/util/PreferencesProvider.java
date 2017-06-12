package org.tetawex.ecf.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.LevelCompletionState;
import org.tetawex.ecf.model.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class PreferencesProvider {
    private static final String PREFERENCES_NAME="ECF Preferences";
    private static final String PREFERENCES_CODE="serialized";
    public static final int LEVELS_COUNT=24;

    public static ECFPreferences getPreferences(){
        Json json=new Json();
        Preferences prefs=Gdx.app.getPreferences(PREFERENCES_NAME);
        ECFPreferences preferences=json.fromJson(ECFPreferences.class,
                prefs.getString(PREFERENCES_CODE));
        if(preferences==null){
            preferences=new ECFPreferences();
            initEmptyPreferences(preferences);
            setPreferences(preferences);
        }
        int size=preferences.getLevelCompletionStateList().size();
        if(size!=LEVELS_COUNT){
            for (int i=LEVELS_COUNT-size-1;i<=LEVELS_COUNT;i++){
                preferences.getLevelCompletionStateList().add(new LevelCompletionState(0,false,false));
            }
        }
        return preferences;

    }
    public static void setPreferences(ECFPreferences preferences){
        Json json=new Json();
        Preferences prefs=Gdx.app.getPreferences(PREFERENCES_NAME);
        prefs.putString(PREFERENCES_CODE,json.toJson(preferences));
        prefs.flush();
    }
    private static void initEmptyPreferences(ECFPreferences preferences){
        preferences.setSelectedLanguageCode("default");
        preferences.setSelectedCountryCode("");
        List<Score> list=new ArrayList<Score>();
        list.add(new Score(9000,"Cirno","1-9, Icicles"));
        list.add(new Score(8000,"Blazing Borkus","1-8, Elementalism"));
        list.add(new Score(6000,"Vivid Turtle","1-4, The Grand Tortoise"));
        list.add(new Score(4000,"Pro WOTE Player","1-10, Elemental Rampage"));
        list.add(new Score(3400,"Casus Beli","1-64, Decimal Fail"));
        list.add(new Score(2007,"Blazer Hero","1-2, Time Machine"));
        list.add(new Score(1000,"Rumia","1-5, Decimal System"));
        list.add(new Score(900,"Cirno","1-15, The Reckoning of Ice"));
        list.add(new Score(300,"Newbie","1-0, Eh? Is that a level?"));
        list.add(new Score(100,"Bitter Butter","1-64, Decimal Fail"));

        List<LevelCompletionState> levelList=new ArrayList<LevelCompletionState>();
        levelList.add(new LevelCompletionState(0,false,true));
        for (int i = 1; i < LEVELS_COUNT; i++) {
            levelList.add(new LevelCompletionState(0,false,false));
        }
        preferences.setLevelCompletionStateList(levelList);
        preferences.setScores(list);
    }
}
