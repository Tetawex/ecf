package org.tetawex.ecf.util;

import com.badlogic.gdx.utils.Json;
import org.tetawex.ecf.model.LevelData;

/**
 * Created by tetawex on 16.08.17.
 */
public class LevelDataUtils {
    public static String toJson(LevelData object){
        return JsonUtils.getSerializer().toJson(new SavableLevelData(object));
    }
    public static LevelData fromJson(String json){
        return JsonUtils.getSerializer().fromJson(json, SavableLevelData.class).createLevelData();
    }
}
