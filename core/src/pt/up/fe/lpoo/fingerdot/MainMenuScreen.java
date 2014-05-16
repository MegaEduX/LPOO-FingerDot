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
    final FingerDot _game;

    public MainMenuScreen(final FingerDot game) {
        _game = game;
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture texture = new Texture(Gdx.files.internal("MAIN_MENU.png"));



        _game.camera.update();

        _game.batch.begin();

       // _game.font.draw(_game.batch, "Welcome to Drop!!! ", 100, 150);
        _game.batch.draw(texture,0,0);
       // _game.font.draw(_game.batch, "Tap anywhere to begin!", 100, 100);
        _game.batch.end();
        int x = Gdx.input.getX(), y = Gdx.input.getY();

        if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
            x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
            y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
        }
        int px1 = 451, px2=799;


        if (Gdx.input.isTouched()){
            //SinglePlayer onClick
            if( px1 <= x && x <= px2 && 240 <= y && y <= 327){
            _game.setScreen(new SinglePlayerScreen(_game));

            dispose();
            }
            //Multiplayer onClick
            if( px1 <= x && x <= px2 && 402 <= y && y <= 494){
                _game.setScreen(new MultiPlayerScreen(_game));

                dispose();
            }
            if( px1 <= x && x <= px2 && 567 <= y && y <= 656){/*
                _game.setScreen(new LeaderBoardScreen(_game));

                dispose();
                */
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

    }
}
