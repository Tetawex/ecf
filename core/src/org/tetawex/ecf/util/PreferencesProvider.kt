package org.tetawex.ecf.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import org.tetawex.ecf.model.ECFPreferences
import org.tetawex.ecf.model.LevelCompletionState
import org.tetawex.ecf.model.Score

import java.util.ArrayList

/**
 * Created by Tetawex on 03.06.2017.
 */
object PreferencesProvider {
    private val PREFERENCES_NAME = "ECF Preferences"
    private val PREFERENCES_CODE = "serialized"
    val LEVELS_COUNT = 18
    val MOT_LEVELS_COUNT = 15
    private var preferences: ECFPreferences? = null
    const val CURRENT_VERSION_CODE = 10

    fun getPreferences(): ECFPreferences {
        if (preferences == null) {
            val json = Json()
            val prefs = Gdx.app.getPreferences(PREFERENCES_NAME)
            preferences = json.fromJson(ECFPreferences::class.java,
                    prefs.getString(PREFERENCES_CODE))
            if (preferences == null) {
                preferences = ECFPreferences()
                initEmptyPreferences(preferences!!)
            }

            if (preferences?.version ?: 0 < 10) {
                preferences!!.scores = getDefaultScores()
            }

            if (preferences?.version ?: 0 < CURRENT_VERSION_CODE) {
                preferences!!.version = CURRENT_VERSION_CODE
            }

            var size = preferences!!.getLevelCompletionStateList("")!!.size
            if (size < LEVELS_COUNT) {
                for (i in 0 until MOT_LEVELS_COUNT - size) {
                    preferences!!.getLevelCompletionStateList("")!!.add(LevelCompletionState(0, false, false))
                }
                preferences!!.getLevelCompletionStateList("")!![0].unlocked = true
            }
            size = preferences!!.getLevelCompletionStateList("mot")!!.size
            if (size < MOT_LEVELS_COUNT) {
                for (i in 0 until MOT_LEVELS_COUNT - size) {
                    preferences!!.getLevelCompletionStateList("mot")!!.add(LevelCompletionState(0, false, false))
                }
                preferences!!.getLevelCompletionStateList("mot")!![0].unlocked = true
            }
        }
        return preferences!!
    }

    fun flushPreferences() {
        val json = Json()
        val prefs = Gdx.app.getPreferences(PREFERENCES_NAME)
        prefs.putString(PREFERENCES_CODE, json.toJson(getPreferences()))
        prefs.flush()
    }

    private fun initEmptyPreferences(preferences: ECFPreferences) {
        preferences.selectedLanguageCode = "default"
        preferences.selectedCountryCode = ""
        val list = getDefaultScores()

        val levelList = ArrayList<LevelCompletionState>()
        levelList.add(LevelCompletionState(0, false, true))
        for (i in 1 until LEVELS_COUNT) {
            levelList.add(LevelCompletionState(3, false, false))
        }
        preferences.setLevelCompletionStateList(levelList, "")
        preferences.scores = list

        //init dlc levels
        val levelList2 = ArrayList<LevelCompletionState>()
        levelList2.add(LevelCompletionState(0, false, true))
        for (i in 1 until MOT_LEVELS_COUNT) {
            levelList2.add(LevelCompletionState(0, false, false))
        }
        preferences.setLevelCompletionStateList(levelList2, "mot")
    }

    private fun getDefaultScores(): MutableList<Score> {
        val list = ArrayList<Score>()
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "---", ""))
        list.add(Score(0, "Invisible Entry", "???"))
        return list
    }

    fun getLevelCountForCode(code: String): Int {
        return if ("mot" == code)
            MOT_LEVELS_COUNT
        else
            LEVELS_COUNT
    }
}
