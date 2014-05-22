package pt.up.fe.lpoo.fingerdot.logic.common;

/**
 * Created by MegaEduX on 20/05/14.
 */

public class OpponentDot extends Dot {
    private boolean _correct;

    private OpponentDot() {

    }

    public OpponentDot(int x, int y, int radius, boolean correct) {
        super(x, y, radius);

        _correct = correct;
    }

    public boolean didTouch(int x, int y) {
        return Math.pow((x - _x), 2) + Math.pow((y - _y), 2) < Math.pow(_radius, 2);
    }
}
