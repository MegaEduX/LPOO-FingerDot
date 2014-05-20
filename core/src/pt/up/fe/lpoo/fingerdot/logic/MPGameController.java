package pt.up.fe.lpoo.fingerdot.logic;

import java.util.ArrayList;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

public class MPGameController extends GameController {
    protected int _opponentScore;
    protected int _opponentLives;

    private ArrayList<Dot> _dots;

    public MPGameController(final FingerDot game, int level, int lives) {
        super(game, level, lives);
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
}
