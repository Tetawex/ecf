package org.tetawex.ecf.util

import com.badlogic.gdx.utils.Json
import org.tetawex.ecf.model.LevelData

/**
 * Created by tetawex on 16.08.17.
 */
object LevelDataUtils {
    fun toJson(data: LevelData): String {
        return JsonUtils.serializer.toJson(SavableLevelData(data))
    }

    fun fromJson(json: String): LevelData {
        return JsonUtils.serializer.fromJson(json, SavableLevelData::class.java).createLevelData()
    }
}
