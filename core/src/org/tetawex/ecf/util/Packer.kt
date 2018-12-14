package org.tetawex.ecf.util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.tools.texturepacker.TexturePacker

object Packer {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val settings = TexturePacker.Settings()
        settings.filterMin = Texture.TextureFilter.Linear
        settings.filterMag = Texture.TextureFilter.Linear
        TexturePacker.process(settings, "assets/textures", "assets", "atlas")
    }
}
