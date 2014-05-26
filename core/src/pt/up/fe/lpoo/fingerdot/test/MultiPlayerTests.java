package pt.up.fe.lpoo.fingerdot.test;

import org.junit.*;

import static org.junit.Assert.*;

public class MultiPlayerTests {
    @Before public void setUp() throws Exception {

    }

    @Test public void generateGame() {
        //  Tests for the ability to generate the dots of a new game.
    }

    @Test public void receiveGame() {
        //  Tests for the ability to receive and correctly parse the dots of a new game.
    }

    @Test public void opponentTappedDot() {
        //  Tests for what happens when the opponent taps a dot.
    }

    @Test public void tapOpponentDot() {
        //  Tests for what happens when the player taps a dot already tapped by the opponent.
    }

    @Test public void playerWinConditionByPoints() {
        //  Tests for the ability for a player to win by having more points than his opponent.
    }

    @Test public void playerWinConditionByOpponentLives() {
        //  Tests for the ability for a player to win by not losing all its life before his opponent.
    }
}