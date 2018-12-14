package org.tetawex.ecf.model

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import org.tetawex.ecf.util.IntVector2
import org.tetawex.ecf.util.LevelDataUtils

/**
 * Created by Tetawex on 06.06.2017.
 */
object LevelFactory {
    fun generateLevel(number: Int, levelCode: String): LevelData {
        val levelData = LevelDataUtils
                .fromJson(readFile("levels/" + levelCode + "level" + number + ".json"))
        levelData.levelNumber = number
        levelData.levelCode = levelCode
        return levelData
    }

    fun convertLevels() {
        for (i in 0..17) {
            Gdx.files.external("ConvertedLevels/level$i.json")
                    .writeString(LevelDataUtils.toJson(generateLevel(i, "")), false)
        }
    }

    @Deprecated("")
    fun generateMotTestingGround(): LevelData {
        val cells = CellArrayFactory.generateBasicCellArray(3, 4)
        cells[1][2]!!.elements!!.add(Element.TIME)
        cells[0][1]!!.elements!!.add(Element.TIME)
        return LevelData(cells, 10, 10000, 0, "The World", "mot")
    }

    private fun readFile(fileName: String): String {
        val handle = Gdx.files.internal(fileName)
        return handle.readString()
    }

    fun generateMotTutorial(): LevelData {
        return LevelDataUtils
                .fromJson(readFile("levels/mottutorial.json"))
    }

    /*
    @Deprecated("")
    fun generateLevelOld(number: Int, levelCode: String): LevelData {

        val jsonReader = JsonReader()
        val jsonData = jsonReader.parse(readFile("levels/" + levelCode + "level" + number + ".json"))
        val height = jsonData.get("height").asInt()
        val width = jsonData.get("width").asInt()
        var mana = 1
        var maxScore = 2000
        var name = ""
        try {
            mana = jsonData.get("properties").get("mana").asInt()
            maxScore = jsonData.get("properties").get("maxScore").asInt()
            name = jsonData.get("properties").get("name").asString()
        } catch (e: Exception) {
        }

        val cellArray = Array<Array<Cell>>(width) { arrayOfNulls(height) }

        val layersIter = jsonData.get("layers").iterator()
        while (layersIter.hasNext()) {
            val layer = layersIter.next()
            val dataIter = layer.get("data").iterator()
            var i = 0
            while (dataIter.hasNext()) {
                val x = i % width
                val y = height - 1 - i / width
                when (dataIter.next().asInt()) {
                    1 -> cellArray[x][y] = CellFactory.generateEmptyCell(IntVector2(x, y))
                    2 -> cellArray[x][y].elements!!.add(Element.FIRE)
                    3 -> cellArray[x][y].elements!!.add(Element.WATER)
                    4 -> cellArray[x][y].elements!!.add(Element.SHADOW)
                    5 -> cellArray[x][y].elements!!.add(Element.LIGHT)
                    6 -> cellArray[x][y].elements!!.add(Element.AIR)
                    7 -> cellArray[x][y].elements!!.add(Element.EARTH)
                    else -> {
                    }
                }
                ++i
            }
        }

        return LevelData(cellArray, mana, maxScore, number, name, levelCode)
    }
    */
}
