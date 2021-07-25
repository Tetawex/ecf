package org.tetawex.ecf.presentation.actor

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.ui.Widget

/**
 * Created by Tetawex on 17.05.2017.
 */
open class BaseWidget<T : Game>(var game: T?) : Widget()
