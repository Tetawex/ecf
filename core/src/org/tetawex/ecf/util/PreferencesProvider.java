package org.tetawex.ecf.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import org.tetawex.ecf.model.ECFPreferences;
import org.tetawex.ecf.model.Score;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tetawex on 03.06.2017.
 */
public class PreferencesProvider {
    private static final String PREFERENCES_NAME="ECF Preferences";
    private static final String PREFERENCES_CODE="serialized";

    public static ECFPreferences getPreferences(){
        Json json=new Json();
        Preferences prefs=Gdx.app.getPreferences(PREFERENCES_NAME);
        ECFPreferences preferences=json.fromJson(ECFPreferences.class,
                prefs.getString(PREFERENCES_CODE));
        if(preferences==null){
            preferences=new ECFPreferences();
            preferences.setSelectedLanguageCode("default");
            List<Score> list=new ArrayList<Score>();
            list.add(new Score(9000,"Cirno","1-9, Icicles"));
            list.add(new Score(2000,"Some kid","1-8, Elementalism"));
        }
        return preferences;

    }
    public static void setPreferences(ECFPreferences preferences){
        Json json=new Json();
        Preferences prefs=Gdx.app.getPreferences(PREFERENCES_NAME);
        prefs.putString(PREFERENCES_CODE,json.toJson(preferences));
        prefs.flush();
    }
}
