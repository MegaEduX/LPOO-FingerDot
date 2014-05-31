package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;

/**
 * Created by MegaEduX on 21/05/14.
 */
public class MultiPlayerMatchmakingScreen implements Screen {
    private MultiPlayerMessenger _msg;

    private Stage _stage = null;

    public MultiPlayerMatchmakingScreen() {
        _msg = new MultiPlayerMessenger();

        _msg.setMultiPlayerMatchmakingScreen(this);

        _msg.start();

        Gdx.input.setCatchBackKey(true);

        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));
        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    private void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String labelText = "Searching for a suitable opponent...";

        BitmapFont.TextBounds bounds = font.getBounds(labelText);

        table.add(new Label(labelText, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

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
    }

    @Override public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _stage.draw();
    }

    public void startGame() {
        System.out.println("Starting MP Game...");

        FingerDot.getSharedInstance().setScreen(new MultiPlayerScreen(_msg));
    }

    public void abortMatchmaking() {
        _msg.stop();
    }

    @Override public void show() {

    }

    @Override public void hide() {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void resize(int x, int y) {

    }

    @Override public void dispose() {
        _stage.dispose();
    }
}
