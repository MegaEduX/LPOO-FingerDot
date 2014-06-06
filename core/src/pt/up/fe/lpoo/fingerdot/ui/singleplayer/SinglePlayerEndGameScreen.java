package pt.up.fe.lpoo.fingerdot.ui.singleplayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.UserManager;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardManager;
import pt.up.fe.lpoo.fingerdot.ui.misc.FontGenerator;
import pt.up.fe.lpoo.fingerdot.ui.misc.MainMenuScreen;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.ui.misc.UserNameSelectionScreen;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class SinglePlayerEndGameScreen implements Screen {
    private final static String kSkinFileName = "uiskin.json";

    private boolean _sentToInternetLeaderboards = false;

    private Stage _stage = null;

    private final SinglePlayerEndGameScreen self;

    int _finalScore = 0;

    public SinglePlayerEndGameScreen(int finalScore) {
        self = this;

        _finalScore = finalScore;

        Gdx.input.setCatchBackKey(true);

        _stage = new Stage(new StretchViewport(FingerDot.getSharedInstance().camera.viewportWidth, FingerDot.getSharedInstance().camera.viewportHeight));

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

        final String scoreString = "Score: " + _finalScore;

        bounds = font.getBounds(scoreString);

        table.add(new Label(scoreString, fontStyle)).width(bounds.width).height(bounds.height).pad(25);

        table.row();

        Skin skin = new Skin(Gdx.files.internal(kSkinFileName));

        TextButton highScoreButton = new TextButton("Submit High Score!", skin);
        table.add(highScoreButton).width(350).height(75).pad(25);

        table.row();

        final String readableDate = DateFormat.getDateTimeInstance().format(new Date());
        final String timeInMillis = String.valueOf(Calendar.getInstance().getTimeInMillis());
        final String version = FingerDot.getSharedInstance().version;

        final LeaderboardEntry localEntry = new LeaderboardEntry(readableDate, timeInMillis, version, _finalScore);

        LeaderboardManager.sharedManager().addLocalScore(localEntry);

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (UserManager.sharedManager().getUser() == null) {
                    FingerDot.getSharedInstance().setScreen(new UserNameSelectionScreen(self, self));
                } else if (!_sentToInternetLeaderboards) {
                    LeaderboardEntry remoteEntry = new LeaderboardEntry(UserManager.sharedManager().getUser().getUsername(), timeInMillis, version, _finalScore);

                    LeaderboardManager.sharedManager().publishScoreOnOnlineLeaderboard(remoteEntry);

                    _sentToInternetLeaderboards = true;
                }
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

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            FingerDot.getSharedInstance().setScreen(new MainMenuScreen());

            dispose();
        }

        _stage.draw();
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(_stage);
    }

    @Override public void hide() {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void resize(int x, int y) {
        _stage.getViewport().update(x, y, false);
    }

    @Override public void dispose() {

    }
}
