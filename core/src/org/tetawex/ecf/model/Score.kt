package org.tetawex.ecf.model

/**
 * Created by Tetawex on 03.06.2017.
 */
class Score {
    var value: Int = 0
    var name: String? = null
    var levelName: String? = null

    constructor(value: Int, name: String, levelName: String) {
        this.value = value
        this.name = name
        this.levelName = levelName
    }

    constructor() {}
}
