package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.WarpController;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;

/**
 * Created by MegaEduX on 21/05/14.
 */
public class MultiPlayerEndGameScreen implements Screen {
    public MultiPlayerEndGameScreen() {
        WarpController.getInstance().stopAppWarp();

        Gdx.input.setCatchBackKey(true);
    }

    @Override public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

            dispose();
        }
    }

    @Override public void show() {

    }

    @Override public void hide() {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {

    }
}
