/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {
    final FingerDot _game;

    OrthographicCamera _camera;

    public MainMenuScreen(final FingerDot game) {
        _game = game;

        _camera = new OrthographicCamera();

        _camera.setToOrtho(false, 800, 480);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _camera.update();
        _game.batch.setProjectionMatrix(_camera.combined);

        _game.batch.begin();
        _game.font.draw(_game.batch, "Welcome to Drop!!! ", 100, 150);
        _game.font.draw(_game.batch, "Tap anywhere to begin!", 100, 100);
        _game.batch.end();

        if (Gdx.input.isTouched()) {
            _game.setScreen(new SinglePlayerScreen(_game));
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
