package org.tetawex.ecf.util

import com.badlogic.gdx.math.RandomXS128

/**
 * Created by Tetawex on 03.05.17.
 */
object RandomProvider {
    private var random: RandomXS128? = null
    private var seed: Long = 0

    fun getRandom(): RandomXS128 {
        if (random == null)
            random = RandomXS128(seed)
        return random!!
    }

    fun setSeed(newSeed: Long) {
        seed = newSeed
        random = RandomXS128(seed)
    }
}
