/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerMatchmakingScreen;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerScreen;
import pt.up.fe.lpoo.fingerdot.ui.singleplayer.SinglePlayerScreen;

import java.io.FileInputStream;

public class MainMenuScreen implements Screen {
    private static final int kButtonWidth = 400;
    private static final int kButtonHeight = 100;

    private static final String kFontFileName = "hecubus.ttf";

    private static final FingerDot _game = FingerDot.getSharedInstance();

    private Texture _menuTexture = null;
    private Skin _skin = null;
    private Stage _stage = null;

    public MainMenuScreen() {
        _menuTexture = new Texture(Gdx.files.internal("main_menu_bg.png"));

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        _skin = new Skin(Gdx.files.internal("uiskin.json"));

        FileHandle fontFile = Gdx.files.internal(kFontFileName);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 32;

        BitmapFont font = generator.generateFont(param);

        generator.dispose();

        Table table = new Table();
        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight - 200);

        Gdx.input.setInputProcessor(_stage);

        TextButton singlePlayer = new TextButton("Singleplayer", _skin);

        //  singlePlayer.getLabel().setStyle(font); not this but near
        table.add(singlePlayer).width(kButtonWidth).height(kButtonHeight).padBottom(20);
        table.row();

        TextButton multiPlayer = new TextButton("Multiplayer",_skin);
        table.add(multiPlayer).width(kButtonWidth).height(kButtonHeight).padBottom(20);
        table.row();

        TextButton leaderBoard = new TextButton("Leaderboard",_skin);
        table.add(leaderBoard).width(kButtonWidth).height(kButtonHeight);
        table.row();

        _stage.addActor(table);

        //onClick Events

        singlePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new SinglePlayerScreen());

                dispose();
            }
        });

        multiPlayer.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new MultiPlayerMatchmakingScreen());

                dispose();
            };
        });

        leaderBoard.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                _game.setScreen(new LeaderboardScreen());

                dispose();
            };
        });

        ImageButton.ImageButtonStyle fbLikeStyle = new ImageButton.ImageButtonStyle();

        fbLikeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fblogo200.png"))));

        ImageButton fbLikeButton = new ImageButton(fbLikeStyle);

        fbLikeButton.setX(1150);
        fbLikeButton.setY(25);

        fbLikeButton.setWidth(100);
        fbLikeButton.setHeight(100);

        fbLikeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://fb.com/FingerDot");
            }
        });

        _stage.addActor(fbLikeButton);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _game.camera.update();

        _game.batch.begin();
        _game.batch.draw(_menuTexture, 0, 0);
        _game.batch.end();

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
        _skin.dispose();
        _stage.dispose();
    }
}
