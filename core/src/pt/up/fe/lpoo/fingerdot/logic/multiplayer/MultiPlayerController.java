package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.SinglePlayerController;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

public class MultiPlayerController extends SinglePlayerController {
    protected int _opponentScore;
    protected int _opponentLives;

    private ArrayList<Dot> _dots;
    private ArrayList<OpponentDot> _opponentTouchedDots = new ArrayList<OpponentDot>();

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

    @Override public void performTick() {
        if (_lives <= 0)
            return;

        if (_dots == null || _dots.size() == 0) {
            //  Display an waiting message? Maybe the game has ended? Whaaaaa.

            return;
        }

        if (_tickCounter > 0)
            _tickCounter--;
        else {
            Dot dot = _dots.get(0);

            _dots.remove(dot);
            _dotsOnPlay.add(dot);

            _tickCounter = _baseTicks / _level;
        }

        Iterator<Dot> iter = _dotsOnPlay.iterator();

        while (iter.hasNext()) {
            Dot dot = iter.next();

            dot.decreaseTicks();

            if (dot.getTicks() <= 0) {
                iter.remove();

                _lives--;
            }
        }

        Iterator<OpponentDot> opIter = _opponentTouchedDots.iterator();

        while (opIter.hasNext()) {
            OpponentDot dot = opIter.next();

            dot.decreaseTicks();

            if (dot.getTicks() <= 0) {
                iter.remove();
            }
        }
    }
}
