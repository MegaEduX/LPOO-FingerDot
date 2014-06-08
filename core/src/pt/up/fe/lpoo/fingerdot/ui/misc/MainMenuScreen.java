//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.UserManager;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerMatchmakingScreen;
import pt.up.fe.lpoo.fingerdot.ui.singleplayer.SinglePlayerScreen;

public class MainMenuScreen implements Screen {
    private static final int kButtonWidth = 400;
    private static final int kButtonHeight = 100;

    private static final String kFontFileName = "hecubus.ttf";

    private static final FingerDot _game = FingerDot.getSharedInstance();

    private Texture _menuTexture = null;
    private Skin _skin = null;
    private Stage _stage = null;

    /**
     * Initializes a main menu screen.
     */

    public MainMenuScreen() {
        drawScene();
    }

    /**
     * Draws the scene internally.
     */

    private void drawScene() {
        if (_menuTexture == null)
            _menuTexture = new Texture(Gdx.files.internal("main_menu_bg.png"));

        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);

        if (_stage == null)
            _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));
        else
            _stage.clear();

        if (_skin == null)
            _skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight - 200);

        Gdx.input.setInputProcessor(_stage);

        TextButton singlePlayer = new TextButton("Singleplayer", _skin);

        table.add(singlePlayer).width(kButtonWidth).height(kButtonHeight).padBottom(20);
        table.row();

        TextButton multiPlayer = new TextButton("Multiplayer", _skin);
        table.add(multiPlayer).width(kButtonWidth).height(kButtonHeight).padBottom(20);
        table.row();

        TextButton leaderBoard = new TextButton("Leaderboard", _skin);
        table.add(leaderBoard).width(kButtonWidth).height(kButtonHeight);
        table.row();

        _stage.addActor(table);

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
                if (!UserManager.sharedManager().internetIsReachable())
                    return;

                if (UserManager.sharedManager().getUser() == null)
                    FingerDot.getSharedInstance().setScreen(new UserNameSelectionScreen(new MainMenuScreen(), new MultiPlayerMatchmakingScreen()));
                else
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

        BitmapFont font = FontGenerator.generateBitmapFont(16);

        String loggedInMessage = (UserManager.sharedManager().getUser() != null ? "Logged in as " + UserManager.sharedManager().getUser().getUsername() + "#" + UserManager.sharedManager().getUser().getPin() + "." : "Not logged in.");

        if (!UserManager.sharedManager().internetIsReachable())
            loggedInMessage = "Not connected to the internet. Network functions disabled.";

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        Label lbl = new Label(loggedInMessage, fontStyle);

        lbl.setX(25);
        lbl.setY(25);

        _stage.addActor(lbl);
    }

    /**
     * Renders the screen.
     *
     * @param delta Delta time since the last call.
     */

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

    /**
     * Called when the screen is shown.
     */

    @Override public void show() {
        Gdx.input.setInputProcessor(_stage);
    }

    /**
     * Called when the screen is hidden.
     */

    @Override public void hide() {

    }

    /**
     * Called when the game is resumed.
     */

    @Override public void resume() {

    }

    /**
     * Called when the game is paused.
     */

    @Override public void pause() {
    }

    /**
     * Called when the window is resized.
     *
     * @param x New x's size.
     * @param y New y's size.
     */

    @Override public void resize(int x, int y) {
        _stage.getViewport().update(x, y, false);
    }

    /**
     * Releases the objects.
     *
     * Don't call this directly.
     */

    @Override public void dispose() {
        _menuTexture.dispose();
        _skin.dispose();
        _stage.dispose();
    }
}
