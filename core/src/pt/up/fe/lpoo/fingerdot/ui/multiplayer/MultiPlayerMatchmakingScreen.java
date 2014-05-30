package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;

/**
 * Created by MegaEduX on 21/05/14.
 */
public class MultiPlayerMatchmakingScreen implements Screen {
    private MultiPlayerMessenger _msg;

    public MultiPlayerMatchmakingScreen() {
        _msg = new MultiPlayerMessenger();

        _msg.setMultiPlayerMatchmakingScreen(this);

        _msg.start();
    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void startGame() {
        System.out.println("Starting MP Game...");

        FingerDot.getSharedInstance().setScreen(new MultiPlayerScreen(_msg));
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
