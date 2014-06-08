//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.*;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.GameBuilder;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.GameGenerator;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.GameGeneratorPart;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.MultiPlayerMessenger;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MultiPlayerTests {
    private Gson _gson = null;

    ArrayList<String> _gameParts = null;

    @Before public void setUp() throws Exception {
        _gson = new Gson();
    }

    /**
     * Tests for the generation of a new game.
     */

    @Test public void generateGame() {
        _gameParts = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            _gameParts.add(_gson.toJson(GameGenerator.generateGamePartWithDots(6, i, 10)));
        }

        assertEquals(_gameParts.size(), 10);
    }

    /**
     * Tests if the game can receive a game from another player.
     */

    @Test public void receiveGame() {
        generateGame();

        GameBuilder gb = new GameBuilder(null);

        for (int i = 0; i < 10; i++) {
            Type gameGeneratorPartType = new TypeToken<GameGeneratorPart>(){}.getType();

            GameGeneratorPart d = _gson.fromJson(_gameParts.get(i), gameGeneratorPartType);

            gb.addPart(d);
        }
    }

    /**
     * Tests a win condition when the other player runs out of lives.
     */

    @Test public void playerWinConditionByOpponentLives() {
        MultiPlayerMessenger mpm = new MultiPlayerMessenger();

        mpm.onGameUpdateReceived("{\"gameOver\": true}");
    }
}