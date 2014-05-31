/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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

    private Skin _skin = null;
    private Stage _stage = null;

    private int _ticksBeforeProcessingTouches = 30;

    public MainMenuScreen() {
        _menuTexture = new Texture(Gdx.files.internal("main_menu_bg.png"));

        _spBounds = new RectangleBounds(450, 800, 230, 310);
        _mpBounds = new RectangleBounds(450, 800, 410, 490);
        _lbBounds = new RectangleBounds(450, 800, 570, 650);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));

        _skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        Gdx.input.setInputProcessor(_stage);

        TextButton singlePlayer = new TextButton("Singleplayer",_skin);
        table.add(singlePlayer).width(250).height(100).padBottom(20);
        table.row();

        TextButton multiPlayer = new TextButton("Multiplayer",_skin);
        table.add(multiPlayer).width(250).height(100).padBottom(20);
        table.row();

        TextButton leaderBoard = new TextButton("Leaderboard",_skin);
        table.add(leaderBoard).width(250).height(100);
        table.row();

        _stage.addActor(table);

        //onClick Events

        singlePlayer.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new SinglePlayerScreen());
            };
        });

        multiPlayer.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new MultiPlayerScreen());
            };
        });

        leaderBoard.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new LeaderboardScreen());
            };
        });

        ImageButton.ImageButtonStyle fbLikeStyle = new ImageButton.ImageButtonStyle();

        fbLikeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("file"))));

        ImageButton fbLikeButton = new ImageButton(fbLikeStyle);


        fbLikeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://fb.com/FingerDot");
            }

            ;
        });
    }

    @Override public void render(float delta) {
        /*Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _game.camera.update();*/

        /*_game.batch.begin();
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
                _game.setScreen(new LeaderboardScreen());

                dispose();

            }
        }*/
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.HOME) || Gdx.input.isKeyPressed(Input.Keys.BACK))
            System.exit(0);


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
