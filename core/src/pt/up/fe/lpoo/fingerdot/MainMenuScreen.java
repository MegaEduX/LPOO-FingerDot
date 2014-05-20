/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import pt.up.fe.lpoo.fingerdot.logic.FingerDot;
import pt.up.fe.lpoo.fingerdot.multiplayer.MultiPlayerScreen;
import pt.up.fe.lpoo.fingerdot.singleplayer.SinglePlayerScreen;

public class MainMenuScreen implements Screen {
    private class RectangleBounds {
        public int startX;
        public int endX;
        public int startY;
        public int endY;

        public RectangleBounds(int sx, int ex, int sy, int ey) {
            startX = sx;
            endX = ex;
            startY = sy;
            endY = ey;
        }

        public boolean isInside(int x, int y) {
            return (x >= startX && x <= endX && y >= startY && y <= endY);
        }
    }

    final FingerDot _game;

    Texture _menuTexture;

    RectangleBounds _spBounds;
    RectangleBounds _mpBounds;
    RectangleBounds _lbBounds;

    public MainMenuScreen(final FingerDot game) {
        _game = game;

        _menuTexture = new Texture(Gdx.files.internal("MAIN_MENU.png"));

        _spBounds = new RectangleBounds(450, 800, 230, 310);
        _mpBounds = new RectangleBounds(450, 800, 410, 490);
        _lbBounds = new RectangleBounds(450, 800, 570, 650);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _game.camera.update();

        _game.batch.begin();
        _game.batch.draw(_menuTexture, 0, 0);
        _game.batch.end();

        int x = Gdx.input.getX(), y = Gdx.input.getY();

        if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
            x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
            y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
        }



        if (Gdx.input.isTouched()) {
            if (_spBounds.isInside(x, y)) {
                _game.setScreen(new SinglePlayerScreen(_game));

                dispose();
            } else if (_mpBounds.isInside(x, y)) {
                _game.setScreen(new MultiPlayerScreen(_game));

                dispose();
            } else if (_lbBounds.isInside(x, y)) {
                /* _game.setScreen(new LeaderBoardScreen(_game));

                dispose();*/
            }
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
        _menuTexture.dispose();
    }
}
