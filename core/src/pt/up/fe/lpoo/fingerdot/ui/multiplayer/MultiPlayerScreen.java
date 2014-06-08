//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerController;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;

public class MultiPlayerScreen implements Screen {
    private final FingerDot _game = FingerDot.getSharedInstance();

    private boolean _stageNeedsUpdate = false;
    private boolean _isTouching = false;

    private MultiPlayerController _controller = null;

    private MultiPlayerMessenger _mpMessenger = null;

    private Stage _stage = null;

    /**
     * Initializes a new multiplayer screen.
     */

    public MultiPlayerScreen() {
        _controller = new MultiPlayerController(_game, 1, 3);
    }

    /**
     * Initializes a new multiplayer screen with a given messenger.
     *
     * @param msg A MultiPlayerMessenger instance.
     */

    public MultiPlayerScreen(MultiPlayerMessenger msg) {
        this();

        _mpMessenger = msg;

        _mpMessenger.setMultiPlayerScreen(this);

        _controller.setMessenger(_mpMessenger);

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    /**
     * Getter for the object's controller.
     *
     * @return A MultiPlayerController instance.
     */

    public MultiPlayerController getController() {
        return _controller;
    }

    /**
     * Draws the stage internally.
     */

    void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, 100);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String scoreMessage = "Score: " + _controller.getScore();

        BitmapFont.TextBounds bounds = font.getBounds(scoreMessage);

        table.add(new Label(scoreMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(100);

        String livesMessage = "Lives: " + _controller.getLives();

        bounds = font.getBounds(livesMessage);

        table.add(new Label(livesMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(100);

        _stage.addActor(table);

        _stageNeedsUpdate = false;
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

        if (_controller.getLives() <= 0) {
            _mpMessenger.broadcastEndOfGame(_controller.getScore(), _controller.dotsLeftCount(), MultiPlayerController.GameState.GameStateLost);

            _controller.setGameState(MultiPlayerController.GameState.GameStateLost);
        }

        if (_controller.getGameState() != MultiPlayerController.GameState.GameStatePlaying) {
            _game.setScreen(new MultiPlayerEndGameScreen(_controller.getGameState()));

            dispose();

            return;
        }

        if (Gdx.input.isTouched() && !_isTouching) {
            int x = Gdx.input.getX(), y = Gdx.input.getY();

            if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
                x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
                y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
            }

            if (y < 570) {
                int points = _controller.performTouch(x, y);

                _mpMessenger.broadcastTouch(x, y, points, points > 0);

                _stageNeedsUpdate = true;

                _isTouching = true;
            }
        }

        if (!Gdx.input.isTouched())
            _isTouching = false;

        int lives = _controller.getLives();

        _controller.performTick();

        if (lives != _controller.getLives())
            _stageNeedsUpdate = true;

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

        if (_stageNeedsUpdate)
            drawStage();

        _stage.act(Gdx.graphics.getDeltaTime());

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
