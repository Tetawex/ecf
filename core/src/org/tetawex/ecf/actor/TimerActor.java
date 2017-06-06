package org.tetawex.ecf.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class TimerActor  extends Actor{
    public interface TimerActorListener{
        void onTimePassed();
    }
    private float timePassed=0f;
    private float timeSpan=0f;

    private TimerActorListener listener;
    public TimerActor(float timeSpan, TimerActorListener listener){
        this.listener=listener;
        this.timeSpan=timeSpan;
    }
    @Override
    public void act(float deltaTime) {
        timePassed+=deltaTime;
        if(timePassed>=timeSpan)
            listener.onTimePassed();
    }
}
