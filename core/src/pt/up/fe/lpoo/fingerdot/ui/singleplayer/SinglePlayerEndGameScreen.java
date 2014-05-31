package pt.up.fe.lpoo.fingerdot.ui.singleplayer;

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
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardManager;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class SinglePlayerEndGameScreen implements Screen {
    private final static String kSkinFileName = "uiskin.json";

    private Stage _stage = null;

    int _finalScore = 0;

    public SinglePlayerEndGameScreen(int finalScore) {
        _finalScore = finalScore;

        Gdx.input.setCatchBackKey(true);

        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));
        Gdx.input.setInputProcessor(_stage);

        drawStage();
    }

    public void drawStage() {
        _stage.getActors().clear();

        Table table = new Table();

        table.setSize(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight);

        BitmapFont headerFont = FontGenerator.generateBitmapFont(60);
        BitmapFont font = FontGenerator.generateBitmapFont(26);

        Label.LabelStyle headerFontStyle = new Label.LabelStyle(headerFont, Color.WHITE);
        Label.LabelStyle fontStyle = new Label.LabelStyle(font, Color.WHITE);

        String gameOverString = "Game Over!";

        BitmapFont.TextBounds bounds = headerFont.getBounds(gameOverString);

        table.add(new Label(gameOverString, headerFontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        String scoreString = "Score: " + _finalScore;

        bounds = font.getBounds(scoreString);

        table.add(new Label(scoreString, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        Skin skin = new Skin(Gdx.files.internal(kSkinFileName));

        TextButton highScoreButton = new TextButton("Submit High Score!", skin);
        table.add(highScoreButton).width(350).height(75).pad(25);

        table.row();

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                /*  LeaderboardEntry entry = new LeaderboardEntry("n", Calendar.getInstance().getTimeInMillis())
                LeaderboardManager.sharedManager().addLocalScore(); */

                //  needs work
            }
        });

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            _stage.addActor(table);

            return;
        }

        TextButton localButton = new TextButton("< Main Menu", skin);
        table.add(localButton).width(250).height(75);

        localButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
