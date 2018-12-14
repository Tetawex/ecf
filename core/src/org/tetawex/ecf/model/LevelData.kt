package org.tetawex.ecf.model

/**
 * Created by Tetawex on 06.06.2017.
 */
class LevelData {
    lateinit var cellArray: Array<Array<Cell?>>
    var mana: Int = 0
    var maxScore: Int = 0
    var levelNumber: Int = 0
    var levelCode: String? = null
    var name: String? = null

    constructor() {}

    constructor(cellArray: Array<Array<Cell?>>, mana: Int, maxScore: Int, levelNumber: Int, name: String, levelCode: String) {
        this.cellArray = cellArray
        this.mana = mana
        this.maxScore = maxScore
        this.levelNumber = levelNumber
        this.name = name
        this.levelCode = levelCode
    }
}
