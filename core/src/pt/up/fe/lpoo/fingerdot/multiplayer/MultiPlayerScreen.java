package pt.up.fe.lpoo.fingerdot.multiplayer;

import com.badlogic.gdx.Screen;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import pt.up.fe.lpoo.fingerdot.logic.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.GameController;

/**
 * Created by MegaEduX on 05/05/14.
 */

public class MultiPlayerScreen implements Screen {
    final FingerDot _game;

    boolean _isTouching = false;

    boolean _paused = false;

    GameController _controller;

    WarpClient _warpClient;

    MultiPlayerMessenger _mpMessenger;

    public static String AppWarpAppKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
    public static String AppWarpSecretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";

    public MultiPlayerScreen(final FingerDot game) {
        _game = game;

        _controller = new GameController(game, 1, 3);

        try {
            WarpClient.initialize(AppWarpAppKey, AppWarpSecretKey);

            _warpClient = WarpClient.getInstance();
        } catch (Exception e) {

        }
    }

    public void renderEnemyTouch(float x, float y, boolean correct) {

    }

    @Override public void render(float delta) {

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

    }
}
