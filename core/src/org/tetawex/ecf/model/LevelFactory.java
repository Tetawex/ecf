package org.tetawex.ecf.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class LevelFactory {
    //TODO Replace with an adequate loading system
    public static LevelData generateLevel(int number){
        LevelData data=new LevelData();
        //Указать параметры
        data.setMana(1);
        data.setMaxScore(4000);
        //Это объект для считывания
        Json json=new Json();
        //Объект для считывания, класс можно сделать например на jsonschema2pojo.org
        YourJsonObject obj=json.fromJson(YourJsonObject.class,
                readFile("levels/level"+number+".json"));

        //...Делаешь какие-то нетривиальные преобразования и возвращаешь LevelData
        return data;



        //Cell[][] cellArray= CellArrayFactory.generateEmptyCellArray(3,3);
        /*if(number==0) {
            cellArray = CellArrayFactory.generateEmptyCellArray(3,3);
            cellArray[0][0] = null;
            cellArray[0][1] = null;
            cellArray[2][0] = null;
            cellArray[2][1] = null;

            cellArray[0][2].setElements(Element.WATER, Element.EARTH);
            cellArray[1][1].setElements(Element.FIRE, Element.EARTH, Element.SHADOW);
            cellArray[1][0].setElements(Element.FIRE, Element.AIR, Element.LIGHT);
            cellArray[2][2].setElements(Element.WATER);
            cellArray[1][2].setElements(Element.AIR);
        }
        else if(number==1) {
            cellArray = CellArrayFactory.generateEmptyCellArray(3,3);
            cellArray[0][2] = null;
            cellArray[2][2] = null;
            cellArray[2][0] = null;

            cellArray[0][0].setElements(Element.WATER, Element.AIR, Element.LIGHT);
            cellArray[1][0].setElements(Element.SHADOW, Element.EARTH);
            cellArray[2][1].setElements(Element.AIR, Element.SHADOW, Element.WATER);
            cellArray[0][1].setElements(Element.LIGHT, Element.AIR, Element.FIRE);
            cellArray[1][1].setElements(Element.LIGHT, Element.EARTH, Element.FIRE);
            cellArray[1][2].setElements(Element.SHADOW, Element.WATER);
        }
        else if(number==2) {
            cellArray = CellArrayFactory.generateEmptyCellArray(3,3);
            cellArray[0][2] = null;
            cellArray[2][0] = null;

            cellArray[0][0].setElements(Element.SHADOW);
            cellArray[0][1].setElements(Element.FIRE);
            cellArray[1][0].setElements(Element.EARTH, Element.SHADOW);
            cellArray[1][1].setElements(Element.SHADOW, Element.AIR);
            cellArray[1][2].setElements(Element.LIGHT, Element.WATER, Element.AIR);
            cellArray[2][1].setElements(Element.FIRE, Element.SHADOW, Element.EARTH);
            cellArray[2][2].setElements(Element.WATER, Element.SHADOW);
        }
        else if(number==3) {
            cellArray = CellArrayFactory.generateEmptyCellArray(3,3);
            cellArray[0][0] = null;
            cellArray[2][0] = null;

            cellArray[0][1].setElements(Element.SHADOW, Element.AIR);
            cellArray[0][2].setElements(Element.FIRE, Element.EARTH);
            cellArray[1][0].setElements(Element.LIGHT, Element.WATER);
            cellArray[1][1].setElements(Element.SHADOW, Element.EARTH, Element.FIRE);
            cellArray[1][2].setElements(Element.LIGHT, Element.WATER);
            cellArray[2][1].setElements(Element.SHADOW, Element.AIR);
            cellArray[2][2].setElements(Element.EARTH, Element.FIRE);
        }
        data.setCellArray(cellArray);*/
    }
    private static String readFile(String fileName){
        FileHandle handle = Gdx.files.internal(fileName);
        return handle.readString();
    }
}
