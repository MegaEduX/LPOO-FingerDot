package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FingerDotClass extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    Boolean showMore = false;
    ArrayList<Dot> _dots = new ArrayList<Dot>();
    int _cbCounter = 10;
    boolean _isTouching = false;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
        if (Gdx.input.isTouched() && !_isTouching) {
            _isTouching = true;
            boolean correct = false;

            Iterator<Dot> iter = _dots.iterator();

            while (iter.hasNext()) {
                Dot dot = iter.next();

                if (dot.didTouch(Gdx.input.getX(), Gdx.input.getY())) {
                    correct = true;

                    System.out.println("Won " + dot.getScore() + " points!");

                    iter.remove();

                    break;
                }
            }

            if (!correct)
                System.out.println("Lost a life...");
        }

        if (!Gdx.input.isTouched())
            _isTouching = false;

        Random rd = new Random();

        Iterator<Dot> iter = _dots.iterator();

        while (iter.hasNext()) {
            Dot dot = iter.next();

            dot.decreaseTicks();

            if (dot.getTicks() <= 0)
                iter.remove();
        }

        if (_cbCounter > 0)
            _cbCounter--;
        else {
            Dot dot = new Dot(rd.nextInt(800), rd.nextInt(480), rd.nextInt(75) + 25);

            _dots.add(dot);

            _cbCounter = 10;
        }

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Dot aDot : _dots) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            shapeRenderer.setColor(1, 0, 0, (float)aDot.getTicks() / 100.0f);

            shapeRenderer.circle(aDot.getX(), aDot.getY(), aDot.getRadius());
        }

        shapeRenderer.end();
	}
}
