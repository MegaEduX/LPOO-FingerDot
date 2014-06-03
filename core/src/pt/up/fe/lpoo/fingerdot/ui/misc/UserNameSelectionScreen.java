package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.User;
import pt.up.fe.lpoo.fingerdot.logic.common.UserManager;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.WarpController;

/**
 * Created by MegaEduX on 03/06/14.
 */

public class UserNameSelectionScreen implements Screen {
    private static final String kFontFileName = "hecubus.ttf";

    private Skin _skin = null;
    private Stage _stage = null;

    private boolean _needsRedraw = false;

    private Screen _callerScreen = null;

    public UserNameSelectionScreen(Screen callerScreen) {
        Gdx.input.setCatchBackKey(true);

        _skin = new Skin(Gdx.files.internal("uiskin.json"));
        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        Gdx.input.setInputProcessor(_stage);

        _needsRedraw = true;

        _callerScreen = callerScreen;
    }

    private void drawStage() {
        _needsRedraw = false;

        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String insertMessage = "Choose an Username:";

        BitmapFont.TextBounds bounds = font.getBounds(insertMessage);

        table.add(new Label(insertMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        final TextField textfield = new TextField("", _skin);

        table.add(textfield).width(FingerDot.getSharedInstance().camera.viewportWidth / 2).height(textfield.getHeight());

        table.row();

        font = FontGenerator.generateBitmapFont(18);

        fontStyle = new Label.LabelStyle(font, Color.WHITE);

        insertMessage = "Existing Users: Login as Username#PIN, Example: MyCoolUsername#1234";

        bounds = font.getBounds(insertMessage);

        table.add(new Label(insertMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        TextButton singlePlayer = new TextButton("Save!", _skin);

        singlePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String[] split = textfield.getText().split("#");

                String pin = "";

                try {
                    pin = split[1];
                } catch (Exception exc) {
                    pin = "";
                }

                if (UserManager.sharedManager().login(new User(split[0], pin))) {
                    FingerDot.getSharedInstance().setScreen(_callerScreen);
                } else {
                    textfield.setText("Username invalid or in use!");
                }
            }
        });

        table.add(singlePlayer).width(200).height(50).padBottom(20);

        _stage.addActor(table);
    }

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

    }
}
