package org.tetawex.ecf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.tetawex.ecf.core.ActionResolverAdapter;
import org.tetawex.ecf.core.ECFGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS=0;
		config.width=432;
		config.height=768;
		new LwjglApplication(new ECFGame(new ActionResolverAdapter()), config);
	}
}
