//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.common;

public class OpponentDot extends Dot {
    private boolean _correct;

    private OpponentDot() {

    }

    /**
     * Intializes a dot, touched by the opponent on a multiplayer game.
     *
     * @param x The X coordinate of the center of the dot.
     * @param y The Y coordinate of the center of the dot.
     * @param radius The radius of the dot.
     * @param correct Whether the opponent touched the dot correctly or not.
     */

    public OpponentDot(int x, int y, int radius, boolean correct) {
        super(x, y, radius);

        _correct = correct;
    }

    /**
     * Getter for the "correct-ness" of the dot.
     *
     * @return The "correct-ness" of the dot.
     */

    public boolean getCorrect() {
        return _correct;
    }
}
