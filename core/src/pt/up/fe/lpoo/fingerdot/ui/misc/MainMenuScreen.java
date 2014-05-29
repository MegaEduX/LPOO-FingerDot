/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerMatchmakingScreen;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerScreen;
import pt.up.fe.lpoo.fingerdot.ui.singleplayer.SinglePlayerScreen;

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

    final FingerDot _game = FingerDot.getSharedInstance();

    Texture _menuTexture;

    RectangleBounds _spBounds;
    RectangleBounds _mpBounds;
    RectangleBounds _lbBounds;

    private int _ticksBeforeProcessingTouches = 30;

    public MainMenuScreen() {
        _menuTexture = new Texture(Gdx.files.internal("main_menu_bg.png"));

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

        if (_ticksBeforeProcessingTouches > 0) {
            _ticksBeforeProcessingTouches--;

            return;
        }

        int x = Gdx.input.getX(), y = Gdx.input.getY();

        if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
            x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
            y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
        }

        if (Gdx.input.isTouched()) {
            if (_spBounds.isInside(x, y)) {
                _game.setScreen(new SinglePlayerScreen());

                dispose();
            } else if (_mpBounds.isInside(x, y)) {
                _game.setScreen(new MultiPlayerMatchmakingScreen());

                dispose();
            } else if (_lbBounds.isInside(x, y)) {
                /* _game.setScreen(new LeaderBoardScreen());

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
