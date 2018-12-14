package org.tetawex.ecf.model

/**
 * Created by Tetawex on 02.05.17.
 */
enum class Element {
    FIRE, WATER, LIGHT, SHADOW, EARTH, AIR, TIME;


    companion object {

        val elementsCount: Int
            get() = values().size

        fun getOpposite(element: Element): Element {
            when (element) {
                WATER -> return FIRE
                FIRE -> return WATER
                EARTH -> return AIR
                AIR -> return EARTH
                LIGHT -> return SHADOW
                SHADOW -> return LIGHT
            }
            return TIME
        }

        fun getElementById(id: Int): Element {
            when (id) {
                1 -> return FIRE
                2 -> return WATER
                3 -> return AIR
                4 -> return EARTH
                5 -> return SHADOW
                6 -> return LIGHT
            }
            return TIME
        }
    }
}
