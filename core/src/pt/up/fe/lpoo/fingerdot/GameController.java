package pt.up.fe.lpoo.fingerdot;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * FingerDot
 *
 * Created by MegaEduX on 02/05/14.
 */

public class GameController {
    static private int _baseAdvanceLevel = 10;

    private ArrayList<Dot> _dotsOnPlay;

    private Random _rng;

    private int _lives;

    private int _score;

    private int _level;

    private int _tickCounter;

    private int _leftToAdvanceLevel;

    private float _scoreMultiplier;

    private boolean _paused;

    public GameController(int level, int lives) {
        _lives = lives;
        _level = level;

        _score = 0;

        _leftToAdvanceLevel = _baseAdvanceLevel;
        _scoreMultiplier = 1.0f;

        _dotsOnPlay = new ArrayList<Dot>();
        _rng = new Random();
    }

    public void performTick() {
        if (_tickCounter > 0)
            _tickCounter--;
        else {
            Dot dot = new Dot(_rng.nextInt(Gdx.graphics.getWidth()), _rng.nextInt(Gdx.graphics.getHeight()), _rng.nextInt(75) + 25);

            _dotsOnPlay.add(dot);

            _tickCounter = 60 / _level;
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
    }

    public void performTouch(int xCoordinate, int yCoordinate) {
        boolean correct = false;

        Iterator<Dot> iter = _dotsOnPlay.iterator();

        while (iter.hasNext()) {
            Dot dot = iter.next();

            if (dot.didTouch(xCoordinate, Gdx.graphics.getHeight() - yCoordinate)) {
                correct = true;

                _score += (dot.getScore() * _scoreMultiplier);

                iter.remove();

                synchronized (this) {
                    _leftToAdvanceLevel--;

                    if (_leftToAdvanceLevel <= 0) {
                        increaseLevel();
                    }
                }

                break;
            }
        }

        if (!correct)
            _lives--;
    }

    public int getLives() {
        return _lives;
    }

    public int getScore() {
        return _score;
    }

    public int getLevel() {
        return _level;
    }

    public void increaseLife() {
        _lives++;
    }

    public void decreaseLife() {
        _lives--;
    }

    public void setLives(int lives) {
        _lives = lives;
    }

    public void increaseScore(int score) {
        _score += score;
    }

    public void decreaseScore(int score) {
        _score -= score;
    }

    public void setScore(int score) {
        _score = score;
    }

    public void increaseLevel() {
        _level++;

        _lives++;

        _scoreMultiplier += 0.5f;

        _leftToAdvanceLevel = _baseAdvanceLevel * _level;
    }

    public void decreaseLevel() {
        _level--;

        _scoreMultiplier -= 0.5f;

        _leftToAdvanceLevel = _baseAdvanceLevel * _level;
    }

    public void setPaused(boolean paused) {
        _paused = paused;
    }

    public void togglePaused() {
        _paused = !_paused;
    }

    public boolean getPaused() {
        return _paused;
    }

    public ArrayList<Dot> getDotsOnPlay() {
        return _dotsOnPlay;
    }
}
