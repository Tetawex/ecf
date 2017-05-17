package org.tetawex.ecf.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.tetawex.ecf.model.Cell;
import org.tetawex.ecf.model.Element;
import org.tetawex.ecf.util.RandomProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tetawex on 02.05.17.
 */
public class HexMapActor extends Actor {
    private Cell[][] cellArray;
    private RandomXS128 random= RandomProvider.getRandom();
    public HexMapActor(int width,int height){
        for (int i = 0; i <width ; i++) {
            for (int j = 0; j <height; j++) {
                cellArray[i][j]=new Cell();
            }
        }
    }
    @Override
    public void act(float deltaTime){
    }
    @Override
    public void draw(Batch batch, float parentAlpha){

    }
    private Set<Element> generateRandomElementSet(){
        Set<Element> set=new HashSet<Element>();
        if(random.nextBoolean()){
            if(random.nextBoolean())
                set.add(Element.FIRE);
            else set.add(Element.WATER);
        }
        if(random.nextBoolean()){
            if(random.nextBoolean())
                set.add(Element.EARTH);
            else set.add(Element.WIND);
        }
        if(random.nextBoolean()){
            if(random.nextBoolean())
                set.add(Element.LIGHT);
            else set.add(Element.SHADOW);
        }
        if(set.size()==0){
            set.add(Element.getElementById(random.nextInt()));
        }
        return set;
    }
}
