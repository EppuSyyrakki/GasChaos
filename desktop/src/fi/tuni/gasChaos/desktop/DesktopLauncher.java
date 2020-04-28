package fi.tuni.gasChaos.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tuni.gasChaos.GasChaosMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GasChaosMain(), config);
		config.height = 1280;
		config.width = 720;
		config.forceExit = false;
	}
}
