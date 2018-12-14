package org.tetawex.ecf.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

/**
 * Created by Tetawex on 03.06.2017.
 */
object BitmapFontFactory {
    fun generateSansFont(size: Int): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/font_main.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = size
        val font = generator.generateFont(parameter)
        generator.dispose()
        return font
    }
}
