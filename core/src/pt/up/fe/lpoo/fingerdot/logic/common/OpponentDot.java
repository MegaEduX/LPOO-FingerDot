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

    public OpponentDot(int x, int y, int radius, boolean correct) {
        super(x, y, radius);

        _correct = correct;
    }

    public boolean getCorrect() {
        return _correct;
    }
}
