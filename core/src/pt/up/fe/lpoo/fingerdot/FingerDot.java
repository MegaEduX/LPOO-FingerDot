package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 03/05/14.
 */
public class FingerDot extends Game {
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;
    public OrthographicCamera camera;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        camera.setToOrtho(false, 1280, 720);

        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        renderer.dispose();
    }
}
