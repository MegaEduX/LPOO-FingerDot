//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.test;

import org.junit.*;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.SinglePlayerController;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SinglePlayerTests {
    private SinglePlayerController _controller = null;
    private ArrayList<Dot> _dotsOnPlay = null;

    @Before public void setUp() throws Exception {
        _controller = new SinglePlayerController(1, 3);

        _controller.getDotsOnPlay().add(new Dot(100, 100, 100));

        _dotsOnPlay = _controller.getDotsOnPlay();
    }

    /**
     * Tests a touch inside a dot.
     */

    @Test public void touchInsideBall() {
        assertEquals(_dotsOnPlay.get(0).didTouch(50, 50), true);
    }

    /**
     * Tests a touch outside a dot.
     */

    @Test public void touchOutsideBall() {
        assertEquals(_dotsOnPlay.get(0).didTouch(200, 250), false);
    }

    /**
     * Tests if the game can advance a level correctly.
     */

    @Test public void advanceLevel() {
        int lives = _controller.getLives();

        _controller.increaseLevel();

        assertEquals(_controller.getLives(), lives + 1);
    }

    /**
     * Tests if the game can end.
     */

    @Test public void gameOver() {
        _controller.setLives(0);

        _controller.performTick();  //  performTick() would throw an exception without a window... Unless the game is over!
    }
}