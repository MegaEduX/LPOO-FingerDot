package pt.up.fe.lpoo.fingerdot.logic;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

public class MPGameController extends GameController {
    protected int _opponentScore;
    protected int _opponentLives;

    public MPGameController(final FingerDot game, int level, int lives) {
        super(game, level, lives);
    }
}
