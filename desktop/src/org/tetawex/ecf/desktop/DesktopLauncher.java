package org.tetawex.ecf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.tetawex.ecf.core.ActionResolverAdapter;
import org.tetawex.ecf.core.ECFGame;
import org.tetawex.ecf.model.LevelFactory;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.foregroundFPS = 0;
        config.width = 432;
        config.height = 768;
        config.useHDPI = true;
        new LwjglApplication(new ECFGame(new DesktopActionResolver()), config);
        LevelFactory.convertLevels();
    }
}
