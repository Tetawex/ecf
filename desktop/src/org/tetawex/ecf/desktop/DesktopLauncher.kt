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

//package org.tetawex.ecf.desktop
//
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
//import com.badlogic.gdx.graphics.glutils.HdpiMode
//import org.tetawex.ecf.core.ECFGame
//
//object DesktopLauncher {
//    @JvmStatic
//    fun main(arg: Array<String>) {
//        val config = Lwjgl3ApplicationConfiguration()
//        config.setForegroundFPS(240)
//        config.useVsync(false)
//        config.setWindowSizeLimits(432, 768, 432, 768)
//        config.setHdpiMode(HdpiMode.Logical)
//
//        //config.fullscreen = true
//        Lwjgl3Application(ECFGame(DesktopActionResolver()), config)
//        //LevelFactory.convertLevels()
//    }
//}
