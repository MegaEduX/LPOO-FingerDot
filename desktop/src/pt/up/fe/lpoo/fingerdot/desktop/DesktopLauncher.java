package pt.up.fe.lpoo.fingerdot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pt.up.fe.lpoo.fingerdot.FingerDot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "FingerDot";

        config.width = 1280;
        config.height = 720;

		new LwjglApplication(new FingerDot(), config);
	}
}
