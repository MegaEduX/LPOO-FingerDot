package pt.up.fe.lpoo.fingerdot.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class LeaderboardScreen implements Screen {
    private Stage _stage = null;
    private Skin _skin = null;

    public LeaderboardScreen() {
        _stage = new Stage(new ScreenViewport(FingerDot.getSharedInstance().camera));
        _skin = new Skin(Gdx.files.internal("uiskin.json"));

        Gdx.input.setInputProcessor(_stage);

        Table table = new Table();
        table.setSize(800,480);

        //  Label

        Label position = new Label("#", _skin);
        table.add(position).width(100).height(100);

        Label name = new Label("Nickname", _skin);
        table.add(name).width(150).height(100);

        Label score = new Label("Score", _skin);
        table.add(score).width(150).height(100);

        table.row();

        /*  TextButton startGame = new TextButton("start game",_skin);
        table.add(startGame).width(200).height(50);
        table.row();

        TextButton options = new TextButton("options",_skin);
        table.add(options).width(150).padTop(10).padBottom(3);
        table.row();

        TextButton credits = new TextButton("credits",_skin);
        table.add(credits).width(150);
        table.row();

        TextButton quit = new TextButton("quit",_skin);
        table.add(quit).width(100).padTop(10);  */

        _stage.addActor(table);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(Gdx.graphics.getDeltaTime());
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
        _stage.dispose();
    }
}
