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
    public enum GameState {GameStatePlaying, GameStateWon, GameStateLost, GameStateTie}

    private GameState _gameState = GameState.GameStatePlaying;

    protected int _opponentScore;
    protected int _opponentLives;

    private ArrayList<Dot> _dots;
    private ArrayList<OpponentDot> _opponentTouchedDots = new ArrayList<OpponentDot>();

    private MultiPlayerMessenger _mpMessenger = null;

    private static final int kOpponentFailedDotBaseScore = 100000;

    public MultiPlayerController(final FingerDot game, int level, int lives) {
        super(level, lives);
    }

    public void setMessenger(MultiPlayerMessenger msg) {
        _mpMessenger = msg;
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

    public void setGameState(GameState state) {
        _gameState = state;
    }

    public GameState getGameState() {
        return _gameState;
    }

    public ArrayList<OpponentDot> getOpponentTouchedDots() {
        return _opponentTouchedDots;
    }

    @Override public void performTick() {
        if (_lives <= 0 || _dots == null || _gameState != GameState.GameStatePlaying)
            return;

        if (_dots.size() == 0 && _gameState == GameState.GameStatePlaying) {   //  The game has ended, but not marked as so yet.
            _mpMessenger.broadcastEndOfGame(_score, 0, GameState.GameStateTie);

            _gameState = GameState.GameStateTie;

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

                _mpMessenger.broadcastTouch(dot.getX(), dot.getY(), dot.getBaseScore(), false);

                _lives--;
            }
        }

        Iterator<OpponentDot> opIter = _opponentTouchedDots.iterator();

        while (opIter.hasNext()) {
            OpponentDot dot = opIter.next();

            dot.decreaseTicks();

            if (dot.getTicks() <= 0) {
                opIter.remove();
            }
        }
    }

    @Override public int performTouch(int xCoordinate, int yCoordinate) {
        boolean correct = false;

        int ret = 0;

        Iterator<Dot> iter = _dotsOnPlay.iterator();

        while (iter.hasNext()) {
            Dot dot = iter.next();

            if (dot.didTouch(xCoordinate, (int) (_game.camera.viewportHeight - yCoordinate))) {
                correct = true;

                ret = (int)(dot.getScore() * _scoreMultiplier);

                iter.remove();

                synchronized (this) {
                    _score += ret;

                    _leftToAdvanceLevel--;

                    if (_leftToAdvanceLevel <= 0) {
                        increaseLevel();
                    }
                }

                _mpMessenger.broadcastTouch(xCoordinate, yCoordinate, _score, true);

                break;
            }
        }

        Iterator<OpponentDot> opIter = _opponentTouchedDots.iterator();

        while (opIter.hasNext()) {
            OpponentDot dot = opIter.next();

            if (dot.getCorrect())
                continue;

            if (dot.didTouch(xCoordinate, (int) (_game.camera.viewportHeight - yCoordinate))) {
                correct = true;

                ret = (int)(kOpponentFailedDotBaseScore * _scoreMultiplier);

                synchronized (this) {
                    _score += ret;
                }

                opIter.remove();

                break;
            }
        }

        if (!correct) {
            _lives--;

            _mpMessenger.broadcastTouch(xCoordinate, yCoordinate, 0, false);
        }

        return ret;
    }
}
