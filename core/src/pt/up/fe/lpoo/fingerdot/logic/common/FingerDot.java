//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.common;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;

public class FingerDot extends Game {
    public SpriteBatch batch;
    public ShapeRenderer renderer;
    public BitmapFont font;
    public OrthographicCamera camera;

    public final String version = "0.1-alpha";

    private static FingerDot _sharedInstance;

    public static FingerDot getSharedInstance() {
        return _sharedInstance;
    }

    public void create() {
        if (_sharedInstance == null)
            _sharedInstance = this;
        else
            return;

        batch = new SpriteBatch();
        font = new BitmapFont();
        renderer = new ShapeRenderer();
        camera = new OrthographicCamera();

        camera.setToOrtho(false, 1280, 720);

        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)) {

        }

        this.setScreen(new MainMenuScreen());
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
