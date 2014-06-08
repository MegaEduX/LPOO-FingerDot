//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

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

public class UserNameSelectionScreen implements Screen {
    private static final String kFontFileName = "hecubus.ttf";

    private Skin _skin = null;
    private Stage _stage = null;

    private boolean _needsRedraw = false;

    private Screen _callerScreen = null;
    private Screen _nextScreen = null;

    private Table _table = null;

    private String _headerMessage = "Choose an Username:";
    private String _bottomMessage = "Existing Users: Login as Username#PIN, Example: MyCoolUsername#1234";

    /**
     * Initializer for a new user name selection screen.
     *
     * @param callerScreen The caller screen.
     * @param nextScreen The destination screen.
     */

    public UserNameSelectionScreen(Screen callerScreen, Screen nextScreen) {
        Gdx.input.setCatchBackKey(true);

        _skin = new Skin(Gdx.files.internal("uiskin.json"));
        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

        Gdx.input.setInputProcessor(_stage);

        _needsRedraw = true;

        _callerScreen = callerScreen;
        _nextScreen = nextScreen;
    }

    /**
     * Draws the stage internally.
     */

    private void drawStage() {
        _needsRedraw = false;

        _stage.getActors().clear();

        _table = new Table();

        _table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        BitmapFont.TextBounds bounds = font.getBounds(_headerMessage);

        _table.add(new Label(_headerMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        _table.row();

        final TextField textField = new TextField("", _skin);

        textField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setEditing(true);
            }
        });

        textField.setMessageText("Your cool username here!");

        _table.add(textField).width(FingerDot.getSharedInstance().camera.viewportWidth / 2).height(textField.getHeight());

        _table.row();

        font = FontGenerator.generateBitmapFont(18);

        fontStyle = new Label.LabelStyle(font, Color.WHITE);

        bounds = font.getBounds(_bottomMessage);

        _table.add(new Label(_bottomMessage, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        _table.row();

        TextButton singlePlayer = new TextButton("Save!", _skin);

        singlePlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String[] split = textField.getText().split("#");

                String pin = "";

                try {
                    pin = split[1];
                } catch (Exception exc) {
                    pin = "";
                }

                if (UserManager.sharedManager().login(new User(split[0], pin)))
                    FingerDot.getSharedInstance().setScreen(_nextScreen);
                else {
                    _bottomMessage = "Username invalid or in use!";

                    _needsRedraw = true;
                }
            }
        });

        _table.add(singlePlayer).width(200).height(50).padBottom(20);

        _stage.addActor(_table);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            return;

        Table backTable = new Table();

        TextButton backButton = new TextButton("< Back", _skin);
        backTable.add(backButton).width(150).height(75);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FingerDot.getSharedInstance().setScreen(_callerScreen);

                dispose();
            }
        });

        backTable.setX(150);
        backTable.setY(650);

        _stage.addActor(backTable);
    }

    /**
     * Sets if the text field is being edited, or not.
     *
     * @param editing true if the text field is being edited, false if not.
     */

    private void setEditing(boolean editing) {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            return;

        if (editing) {
            _table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight / 2);

            _table.setX(0);
            _table.setY(FingerDot.getSharedInstance().camera.viewportHeight / 2);
        } else {
            _table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

            _table.setX(0);
            _table.setY(0);
        }
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

        if (_needsRedraw)
            drawStage();

        _stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            FingerDot.getSharedInstance().setScreen(_callerScreen);

            dispose();
        }
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
        _skin.dispose();
    }
}
