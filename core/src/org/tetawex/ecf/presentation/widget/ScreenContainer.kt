package org.tetawex.ecf.presentation.widget

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import org.tetawex.ecf.presentation.VIEWPORT_HEIGHT
import org.tetawex.ecf.presentation.VIEWPORT_WIDTH

class ScreenContainer(vararg children: Actor) : Stack() {
    init {
        setFillParent(true)
        children.forEach {
            add(it)
        }
    }

    override fun getMaxWidth(): Float {
        return VIEWPORT_WIDTH
    }

    override fun getMinHeight(): Float {
        return VIEWPORT_HEIGHT
    }

    override fun getMinWidth(): Float {
        return VIEWPORT_WIDTH
    }

    override fun getPrefWidth(): Float {
        return VIEWPORT_WIDTH
    }

    override fun getPrefHeight(): Float {
        return VIEWPORT_HEIGHT
    }
}