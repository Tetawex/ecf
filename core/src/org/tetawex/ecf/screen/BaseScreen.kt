package org.tetawex.ecf.screen

import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter
import org.tetawex.ecf.core.ECFGame

/**
 * Created by Tetawex on 17.05.2017.
 */
open class BaseScreen<T : Game>(val game: T) : ScreenAdapter() {
    open fun onBackPressed() {}
}
