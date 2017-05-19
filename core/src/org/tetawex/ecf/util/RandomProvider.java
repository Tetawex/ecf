package org.tetawex.ecf.util;

import com.badlogic.gdx.math.RandomXS128;

/**
 * Created by Tetawex on 03.05.17.
 */
public class RandomProvider {
    private static RandomXS128 random;
    private static long seed=0;

    public static RandomXS128 getRandom() {
        if(random==null)
            random=new RandomXS128(seed);
        return random;
    }
    public static void setSeed(long newSeed){
        seed=newSeed;
        random=new RandomXS128(seed);
    }
}
