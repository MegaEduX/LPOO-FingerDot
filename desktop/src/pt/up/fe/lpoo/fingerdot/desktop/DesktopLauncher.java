package pt.up.fe.lpoo.fingerdot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pt.up.fe.lpoo.fingerdot.FingerDotClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "FingerDot";

        config.width = 800;
        config.height = 480;

		new LwjglApplication(new FingerDotClass(), config);
	}
}
