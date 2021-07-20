package org.tetawex.ecf.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.tetawex.ecf.core.ECFGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.foregroundFPS = 240
        config.vSyncEnabled = false
        config.width = 432
        config.height = 768
        config.useHDPI = true

        //config.fullscreen = true
        LwjglApplication(ECFGame(DesktopActionResolver()), config)
        //LevelFactory.convertLevels()
    }
}
