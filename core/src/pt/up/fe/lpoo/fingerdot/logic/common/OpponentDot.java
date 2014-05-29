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

    public boolean getCorrect() {
        return _correct;
    }
}
