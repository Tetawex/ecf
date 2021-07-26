package org.tetawex.ecf.presentation.widget

import com.badlogic.gdx.scenes.scene2d.Actor

/**
 * Created by Tetawex on 06.06.2017.
 */
class TimerActor(val timeSpan: Float, private val listener: TimerActorListener) : Actor() {

    private var timePassed = 0f

    interface TimerActorListener {
        fun onTimePassed()
    }

    override fun act(deltaTime: Float) {
        timePassed += deltaTime
        if (timePassed >= timeSpan)
            listener.onTimePassed()
    }
}
