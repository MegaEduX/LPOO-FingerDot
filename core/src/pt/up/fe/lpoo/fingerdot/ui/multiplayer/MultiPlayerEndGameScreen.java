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
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerController;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.WarpController;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;

public class MultiPlayerEndGameScreen implements Screen {
    private final static String kSkinFileName = "uiskin.json";

    private MultiPlayerController.GameState _endGameState = MultiPlayerController.GameState.GameStatePlaying;

    private Stage _stage = null;

    /**
     * Initializes a new end game screen.
     */

    public MultiPlayerEndGameScreen() {
        WarpController.getInstance().stopAppWarp();

        Gdx.input.setCatchBackKey(true);
    }

    /**
     * Initializes a new end game screen with a given game state.
     *
     * @param gameStatus The game state.
     */

    public MultiPlayerEndGameScreen(MultiPlayerController.GameState gameStatus) {
        this();

        _endGameState = gameStatus;

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    /**
     * Returns a readable game state.
     *
     * @param state The game state.
     * @return A readable string for the given game state.
     */

    private String readableState(MultiPlayerController.GameState state) {
        switch (state) {
            case GameStatePlaying:
                return "Playing";

            case GameStateWon:
                return "Won!";

            case GameStateLost:
                return "Lost...";

            case GameStateTie:
                return "Tie!";

            default:
                return "";
        }
    }

    /**
     * Draws the stage internally.
     */

    public void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont headerFont = FontGenerator.generateBitmapFont(60);
        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle headerFontStyle = new Label.LabelStyle(headerFont, Color.WHITE);
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String gameOverString = "Game Over!";

        BitmapFont.TextBounds bounds = headerFont.getBounds(gameOverString);

        table.add(new Label(gameOverString, headerFontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        final String scoreString = "Game Result: " + readableState(_endGameState);

        bounds = font.getBounds(scoreString);

        table.add(new Label(scoreString, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            _stage.addActor(table);

            return;
        }

        Skin skin = new Skin(Gdx.files.internal(kSkinFileName));

        TextButton localButton = new TextButton("< Main Menu", skin);
        table.add(localButton).width(250).height(75);

        localButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

                dispose();
            }
        });

        _stage.addActor(table);
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

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

            dispose();
        }

        _stage.draw();
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
