package org.tetawex.ecf.presentation.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter

/**
 * Created by Tetawex on 17.05.2017.
 */
open class BaseScreen<T : Game>(val game: T) : ScreenAdapter() {
    open fun onBackPressed(): Boolean {
        return false
    }
}
