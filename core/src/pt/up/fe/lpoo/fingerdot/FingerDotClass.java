package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FingerDotClass extends ApplicationAdapter {
    ArrayList<Dot> _dots;

    int _cbCounter = 60;
    boolean _isTouching = false;
	
	@Override public void create() {
        _dots = new ArrayList<Dot>();
	}

	@Override public void render() {
        BitmapFont font = new BitmapFont();

        SpriteBatch sb = new SpriteBatch();

        sb.begin();

        font.setColor(1, 1, 1, 1);

        font.draw(sb, "Score: XXXX", 200, 200);

        sb.end();

        if (Gdx.input.isTouched() && !_isTouching) {
            System.out.println("Touched Coordinates: " + Gdx.input.getX() + ", " + Gdx.input.getY());
            _isTouching = true;
            boolean correct = false;

            Iterator<Dot> iter = _dots.iterator();

            while (iter.hasNext()) {
                Dot dot = iter.next();

                if (dot.didTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
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

            if (dot.getTicks() <= 0) {
                iter.remove();

                System.out.println("Awww, lost a life...");
            }
        }

        if (_cbCounter > 0)
            _cbCounter--;
        else {
            Dot dot = new Dot(rd.nextInt(Gdx.graphics.getWidth()), rd.nextInt(Gdx.graphics.getHeight()), rd.nextInt(75) + 25);

            _dots.add(dot);

            _cbCounter = 60;
        }

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Dot aDot : _dots) {
            shapeRenderer.setColor(1, 0, 0, (float)aDot.getTicks() / 100.0f);

            shapeRenderer.circle(aDot.getX(), aDot.getY(), aDot.getRadius());
        }

        shapeRenderer.end();
	}
}
