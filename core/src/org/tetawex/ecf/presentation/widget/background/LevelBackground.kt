package org.tetawex.ecf.presentation.widget.background

import org.tetawex.ecf.core.ECFGame

class LevelBackground(game: ECFGame, levelCode: String) : Background(game, levelCode + "background") {
    constructor(game: ECFGame) : this(game, "")
}