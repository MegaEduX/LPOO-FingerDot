package pt.up.fe.lpoo.fingerdot.ui.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.SinglePlayerController;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

public class SinglePlayerScreen implements Screen {
    final FingerDot _game = FingerDot.sharedInstance;

    boolean _isTouching = false;

    boolean _paused = false;

    SinglePlayerController _controller;

    Texture _pausedTexture;

    public SinglePlayerScreen() {
        _controller = new SinglePlayerController(1, 3);

        _pausedTexture = new Texture(Gdx.files.internal("paused.png"));
    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_paused) {
            _game.batch.begin();
            _game.batch.draw(_pausedTexture, 200, 50);
            _game.batch.end();

            if (!_isTouching)
                if (Gdx.input.isTouched()) {
                    _paused = false;

                    _isTouching = true;
                }

            if (!Gdx.input.isTouched())
                _isTouching = false;

            return;
        }

        if (_controller.getLives() <= 0) {
            _game.setScreen(new SinglePlayerEndGameScreen(_controller.getScore()));

            dispose();

            return;
        }

        if (Gdx.input.isTouched() && !_isTouching) {
            int x = Gdx.input.getX(), y = Gdx.input.getY();

            if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
                x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
                y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
            }

            System.out.println("Real X: " + Gdx.input.getX() + ". Computed X: " + x);
            System.out.println("Real Y: " + Gdx.input.getY() + ". Computed Y: " + y);

            if (y > 615 && x > 620 && x < 650)
                pause();
            else
                _controller.performTouch(x, y);

            _isTouching = true;
        }

        if (!Gdx.input.isTouched())
            _isTouching = false;

        _controller.performTick();

        _game.renderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Dot aDot : _controller.getDotsOnPlay()) {
            _game.renderer.setColor(1.0f, 0.0f, 0.0f, (float)aDot.getTicks() / 100.0f);

            _game.renderer.circle(aDot.getX(), aDot.getY(), aDot.getRadius());
        }

        _game.renderer.end();

        _game.batch.begin();

        _game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        _game.font.draw(_game.batch, "Score: " + _controller.getScore(), 25, 50);

        _game.font.draw(_game.batch, "Pause", _game.camera.viewportWidth / 2 - 25, 50);

        _game.font.draw(_game.batch, "Lives: " + _controller.getLives(), _game.camera.viewportWidth - 75, 50);

        _game.batch.end();
    }

    @Override public void show() {

    }

    @Override public void hide() {

    }

    @Override public void resume() {
        _paused = false;
    }

    @Override public void pause() {
        _paused = true;
    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {
        _pausedTexture.dispose();
    }
}
