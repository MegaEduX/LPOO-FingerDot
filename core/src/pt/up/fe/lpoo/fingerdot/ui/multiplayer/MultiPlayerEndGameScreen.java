package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Screen;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.WarpController;

/**
 * Created by MegaEduX on 21/05/14.
 */
public class MultiPlayerEndGameScreen implements Screen {
    public MultiPlayerEndGameScreen() {
        WarpController.getInstance().stopAppWarp();
    }

    @Override public void render(float delta) {

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
