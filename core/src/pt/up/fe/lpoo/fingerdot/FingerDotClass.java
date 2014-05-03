package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class FingerDotClass extends ApplicationAdapter {
    GameController _controller;

    boolean _isTouching = false;
	
	@Override public void create() {
        _controller = new GameController(1, 3);
	}

	@Override public void render() {
        if (Gdx.input.isTouched() && !_isTouching) {
            _controller.performTouch(Gdx.input.getX(), Gdx.input.getY());

            _isTouching = true;
        }

        if (!Gdx.input.isTouched())
            _isTouching = false;

        _controller.performTick();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Dot aDot : _controller.getDotsOnPlay()) {
            shapeRenderer.setColor(1.0f, 0.0f, 0.0f, (float)aDot.getTicks() / 100.0f);

            shapeRenderer.circle(aDot.getX(), aDot.getY(), aDot.getRadius());
        }

        shapeRenderer.end();

        BitmapFont font = new BitmapFont();

        SpriteBatch batch = new SpriteBatch();

        batch.begin();

        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        font.draw(batch, "Score: " + _controller.getScore(), 25, 50);

        font.draw(batch, "Lives: " + _controller.getLives(), Gdx.graphics.getWidth() - 75, 50);

        batch.end();
	}
}
