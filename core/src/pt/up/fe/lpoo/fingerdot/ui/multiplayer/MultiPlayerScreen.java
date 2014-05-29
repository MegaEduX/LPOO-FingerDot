package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerController;

/**
 * Created by MegaEduX on 05/05/14.
 */

public class MultiPlayerScreen implements Screen {
    final FingerDot _game = FingerDot.getSharedInstance();

    boolean _isTouching = false;

    private MultiPlayerController _controller;

    private MultiPlayerMessenger _mpMessenger;

    public MultiPlayerScreen() {
        _controller = new MultiPlayerController(_game, 1, 3);
    }

    public MultiPlayerScreen(MultiPlayerMessenger msg) {
        this();

        _mpMessenger = msg;

        _mpMessenger.setMultiPlayerScreen(this);

        _controller.setMessenger(_mpMessenger);
    }

    public MultiPlayerController getController() {
        return _controller;
    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_controller.getLives() <= 0) {
            _mpMessenger.broadcastLoss();

            _controller.setGameState(false);
        }

        if (!_controller.getGameState()) {
            _game.setScreen(new MultiPlayerEndGameScreen());

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

            if (x > _game.camera.viewportWidth - 75 && x < _game.camera.viewportWidth + 75 && y > 675)
                pause();
            else {
                int points = _controller.performTouch(x, y);

                _mpMessenger.broadcastTouch(x, y, points, points > 0);
            }

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

        for (OpponentDot od : _controller.getOpponentTouchedDots()) {
            _game.renderer.setColor(0.0f, od.getCorrect() ? 0.0f : 1.0f, od.getCorrect() ? 1.0f : 0.0f, (float)od.getTicks() / 100.0f);

            _game.renderer.circle(od.getX(), od.getY(), od.getRadius());
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

    }

    @Override public void pause() {

    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {

    }
}
