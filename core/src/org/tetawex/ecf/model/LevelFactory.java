package org.tetawex.ecf.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedSet;
import com.badlogic.gdx.utils.XmlReader;

import org.tetawex.ecf.util.IntVector2;

/**
 * Created by Tetawex on 06.06.2017.
 */
public class LevelFactory {
    //TODO Replace with an adequate loading system
    public static LevelData generateLevel(int number){
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonData = jsonReader.parse(readFile("levels/level"+number+".json"));
        int height = jsonData.get("height").asInt();
        int width = jsonData.get("width").asInt();
        int mana = jsonData.get("properties").get("mana").asInt();
        Cell[][] cellArray = new Cell[width][height];

        JsonValue.JsonIterator layersIter = jsonData.get("layers").iterator();
        while (layersIter.hasNext()) {
            JsonValue layer = layersIter.next();
            JsonValue.JsonIterator dataIter = layer.get("data").iterator();
            int i = 0;
            while(dataIter.hasNext()){
                int     x = i%width,
                        y = height-1-i/width;
                switch(dataIter.next().asInt()){
                    case 1:  cellArray[x][y] = CellFactory.generateEmptyCell(new IntVector2(x,y));
                        break;
                    case 2:  cellArray[x][y].getElements().add(Element.FIRE);
                        break;
                    case 3:  cellArray[x][y].getElements().add(Element.WATER);
                        break;
                    case 4:  cellArray[x][y].getElements().add(Element.SHADOW);
                        break;
                    case 5:  cellArray[x][y].getElements().add(Element.LIGHT);
                        break;
                    case 6:  cellArray[x][y].getElements().add(Element.AIR);
                        break;
                    case 7:  cellArray[x][y].getElements().add(Element.EARTH);
                        break;
                    default: break;
                }
                ++i;
            }
        }

        LevelData data = new LevelData(cellArray, mana, 4000);

        return data;
    }
    private static String readFile(String fileName){
        FileHandle handle = Gdx.files.internal(fileName);
        return handle.readString();
    }
}