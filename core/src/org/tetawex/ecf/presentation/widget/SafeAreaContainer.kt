package org.tetawex.ecf.presentation.widget

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import org.tetawex.ecf.presentation.VIEWPORT_HEIGHT
import org.tetawex.ecf.presentation.VIEWPORT_WIDTH

class SafeAreaContainer(child: Actor) : Table() {
    init {
        add(child).growY().fillX()
        setFillParent(true)
    }

    override fun getMaxWidth(): Float {
        return VIEWPORT_WIDTH
    }

    override fun getPrefHeight(): Float {
        return VIEWPORT_HEIGHT
    }
}