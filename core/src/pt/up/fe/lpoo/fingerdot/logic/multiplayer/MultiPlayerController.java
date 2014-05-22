package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.SinglePlayerController;

import java.util.ArrayList;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

public class MultiPlayerController extends SinglePlayerController {
    protected int _opponentScore;
    protected int _opponentLives;

    private ArrayList<Dot> _dots;
    private ArrayList<OpponentDot> _opponentTouchedDots;

    public MultiPlayerController(final FingerDot game, int level, int lives) {
        super(level, lives);
    }

    public void addOpponentDot(OpponentDot dot) {
        _opponentTouchedDots.add(dot);
    }

    public void addOpponentScore(int score) {
        _opponentScore += score;
    }

    public void removeOpponentLife() {
        _opponentLives--;
    }

    public void setDots(ArrayList<Dot> list) {
        _dots = list;
    }

    public void addDots(ArrayList<Dot> list) {
        if (_dots == null)
            _dots = new ArrayList<Dot>();

        _dots.addAll(list);
    }
}
