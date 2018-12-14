package org.tetawex.ecf.model

import java.util.ArrayList

/**
 * Created by Tetawex on 03.06.2017.
 */
class ECFPreferences {
    var scores: MutableList<Score> = ArrayList()
    private var levelCompletionStateList: MutableList<LevelCompletionState> = ArrayList()
    private var levelCompletionStateListMot: MutableList<LevelCompletionState> = ArrayList()

    var completedTutorial: Boolean = false
    var completedMotTutorial: Boolean = false

    var soundVolume = 0.5f
    var musicVolume = 0.5f

    var version:Int? = null

    var selectedLanguageCode: String? = null
    var selectedCountryCode: String? = null

    var selectedLanguage: String
        get() = selectedLanguageCode + "_" +
                selectedCountryCode
        set(language) {
            val str = language.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            selectedLanguageCode = str[0]
            if (str.size > 1)
                selectedCountryCode = str[1]
            else
                selectedCountryCode = ""
        }

    fun getLevelCompletionStateList(levelCode: String): MutableList<LevelCompletionState>? {
        if ("" == levelCode)
            return levelCompletionStateList
        else if ("mot" == levelCode)
            return levelCompletionStateListMot
        return null
    }

    fun setLevelCompletionStateList(list: MutableList<LevelCompletionState>, levelCode: String) {
        if ("" == levelCode)
            this.levelCompletionStateList = list
        else if ("mot" == levelCode)
            this.levelCompletionStateListMot = list
    }
}
