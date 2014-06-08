//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;

public class MultiPlayerMatchmakingScreen implements Screen {
    private MultiPlayerMessenger _msg;

    private Stage _stage = null;

    private boolean _needsRedraw = false;

    private boolean _startGameOnNextRun = false;

    private String _currentMessage = "Connecting to server...";

    /**
     * Initializes a matchmaking screen.
     */

    public MultiPlayerMatchmakingScreen() {
        _msg = new MultiPlayerMessenger();

        _msg.setMultiPlayerMatchmakingScreen(this);

        _msg.start();

        Gdx.input.setCatchBackKey(true);

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));
        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    /**
     * Draws the stage internally.
     */

    private void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        BitmapFont.TextBounds bounds = font.getBounds(_currentMessage);

        table.add(new Label(_currentMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            _stage.addActor(table);

            return;
        }

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton localButton = new TextButton("< Back", skin);
        table.add(localButton).width(250).height(75);

        localButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                abortMatchmaking();

                FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

                dispose();
            }
        });

        _stage.addActor(table);

        _needsRedraw = false;
    }

    /**
     * Sets the current on-screen message.
     *
     * @param message The on-screen message to present.
     */

    public void setCurrentMessage(String message) {
        _currentMessage = message;

        _needsRedraw = true;
    }

    /**
     * Renders the screen.
     *
     * @param delta Delta time since the last call.
     */

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_needsRedraw)
            drawStage();

        _stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

            dispose();
        }

        if (_startGameOnNextRun) {
            System.out.println("Starting MP Game...");

            FingerDot.getSharedInstance().setScreen(new MultiPlayerScreen(_msg));

            dispose();
        }
    }

    /**
     * Starts the multiplayer game.
     */

    public void startGame() {
        _startGameOnNextRun = true;
    }

    /**
     * Aborts the matchmaking session.
     */

    public void abortMatchmaking() {
        _msg.stop();
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
        _stage.dispose();
    }
}
