package pt.up.fe.lpoo.fingerdot.ui.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.SinglePlayerController;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */

public class SinglePlayerScreen implements Screen {
    private final FingerDot _game = FingerDot.getSharedInstance();

    private boolean _stageNeedsUpdate = false;
    private boolean _isTouching = false;
    private boolean _paused = false;

    private SinglePlayerController _controller = null;

    private Texture _pausedTexture = null;

    private Stage _stage = null;

    public SinglePlayerScreen() {
        _controller = new SinglePlayerController(1, 3);

        _pausedTexture = new Texture(Gdx.files.internal("paused.png"));

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, 100);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String scoreMessage = "Score: " + _controller.getScore();

        BitmapFont.TextBounds bounds = font.getBounds(scoreMessage);

        table.add(new Label(scoreMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(100);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton localButton = new TextButton("Pause Game", skin);
        table.add(localButton).width(250).height(75);

        localButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
            }
        });

        String livesMessage = "Lives: " + _controller.getLives();

        bounds = font.getBounds(livesMessage);

        table.add(new Label(livesMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(100);

        _stage.addActor(table);

        _stageNeedsUpdate = false;
    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (_paused) {
            _game.batch.begin();
            _game.batch.draw(_pausedTexture, 200, 50);
            _game.batch.end();

            if (!_isTouching)
                if (Gdx.input.isTouched()) {
                    _paused = false;

                    _isTouching = true;
                }

            if (!Gdx.input.isTouched())
                _isTouching = false;

            return;
        }

        if (_controller.getLives() <= 0) {
            _game.setScreen(new SinglePlayerEndGameScreen(_controller.getScore()));

            dispose();

            return;
        }

        if (Gdx.input.isTouched() && !_isTouching) {
            int x = Gdx.input.getX(), y = Gdx.input.getY();

            if (!(_game.camera.viewportWidth == Gdx.graphics.getWidth() && _game.camera.viewportHeight == Gdx.graphics.getHeight())) {
                x *= _game.camera.viewportWidth / Gdx.graphics.getWidth();
                y *= _game.camera.viewportHeight / Gdx.graphics.getHeight();
            }

            System.out.println("Camera width: " + _game.camera.viewportWidth);

            System.out.println("Real X: " + Gdx.input.getX() + ". Computed X: " + x);
            System.out.println("Real Y: " + Gdx.input.getY() + ". Computed Y: " + y);

            if (y < 570) {
                _controller.performTouch(x, y);

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

        _game.renderer.end();

        if (_stageNeedsUpdate)
            drawStage();

        _stage.act(Gdx.graphics.getDeltaTime());

        _stage.draw();
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
        _stage.getViewport().update(x, y, false);
    }

    @Override public void dispose() {
        _pausedTexture.dispose();
    }
}
