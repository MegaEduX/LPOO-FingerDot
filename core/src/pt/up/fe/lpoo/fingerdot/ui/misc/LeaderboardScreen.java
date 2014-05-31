package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardManager;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class LeaderboardScreen implements Screen {
    private static final int kTableHeightHeader = 100;
    private static final int kTableHeightNormal = 40;

    private static final String kFontFileName = "hecubus.ttf";

    private Stage _stage = null;

    private boolean _showOnline = false;

    public LeaderboardScreen() {
        Gdx.input.setCatchBackKey(true);

        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));
        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    private void drawStage() {
        _stage.getActors().clear();

        Table hsTable = new Table();
        Table optTable = new Table();
        Table backTable = new Table();

        hsTable.setSize(FingerDot.getSharedInstance().camera.viewportWidth - 125, FingerDot.getSharedInstance().camera.viewportHeight - 250);

        hsTable.setX(125);
        hsTable.setY(100);

        FileHandle fontFile = Gdx.files.internal(kFontFileName);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 42;

        BitmapFont headerFont = generator.generateFont(param);

        param.size = 26;

        BitmapFont font = generator.generateFont(param);

        generator.dispose();

        Label.LabelStyle fontStyle = new Label.LabelStyle(headerFont, Color.WHITE);

        hsTable.add(new Label("#", fontStyle)).width(50).height(kTableHeightHeader);

        hsTable.add(new Label("Nickname", fontStyle)).width(500).height(kTableHeightHeader);

        hsTable.add(new Label("Score", fontStyle)).width(300).height(kTableHeightHeader);

        hsTable.row();

        fontStyle = new Label.LabelStyle(font, Color.WHITE);

        int posCounter = 1;

        if (_showOnline) {
            if (LeaderboardManager.sharedManager().retrieveOnlineLeaderboard()) {
                for (LeaderboardEntry lbe : LeaderboardManager.sharedManager().getOnlineLeaderboard()) {
                    hsTable.add(new Label(Integer.toString(posCounter), fontStyle)).width(50).height(kTableHeightNormal);
                    hsTable.add(new Label(lbe.username, fontStyle)).width(500).height(kTableHeightNormal);
                    hsTable.add(new Label(Integer.toString(lbe.score), fontStyle)).width(300).height(kTableHeightNormal);

                    hsTable.row();

                    posCounter++;
                }
            }
        } else {
            if (LeaderboardManager.sharedManager().loadLocalLeaderboard()) {
                for (LeaderboardEntry lbe : LeaderboardManager.sharedManager().getLocalLeaderboard()) {
                    hsTable.add(new Label(Integer.toString(posCounter), fontStyle)).width(50).height(kTableHeightNormal);
                    hsTable.add(new Label(lbe.username, fontStyle)).width(500).height(kTableHeightNormal);
                    hsTable.add(new Label(Integer.toString(lbe.score), fontStyle)).width(300).height(kTableHeightNormal);

                    hsTable.row();

                    posCounter++;
                }
            }
        }

        while (posCounter != 10) {
            hsTable.add(new Label(Integer.toString(posCounter), fontStyle)).width(50).height(kTableHeightNormal);
            hsTable.add(new Label("???", fontStyle)).width(500).height(kTableHeightNormal);
            hsTable.add(new Label("0", fontStyle)).width(300).height(kTableHeightNormal);

            hsTable.row();

            posCounter++;
        }

        _stage.addActor(hsTable);

        optTable.setSize(600, 100);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton localButton = new TextButton("Local", skin);
        optTable.add(localButton).width(250).height(75);

        localButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (_showOnline) {
                    _showOnline = false;

                    drawStage();
                }
            }
        });

        TextButton mpButton = new TextButton("Online", skin);
        optTable.add(mpButton).width(250).height(75);

        mpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!_showOnline) {
                    _showOnline = true;

                    drawStage();
                }
            }
        });

        optTable.setX(335);
        optTable.setY(525);

        _stage.addActor(optTable);

        if (Gdx.app.getType() == Application.ApplicationType.Android)
            return;

        TextButton backButton = new TextButton("Back", skin);
        backTable.add(backButton).width(150).height(75);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

                dispose();
            }
        });

        backTable.setX(150);
        backTable.setY(575);

        _stage.addActor(backTable);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _stage.act(Gdx.graphics.getDeltaTime());
        _stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());
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
