//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SinglePlayerController {
    protected final FingerDot _game = FingerDot.getSharedInstance();

    static protected int _baseAdvanceLevel = 20;
    static protected int _baseTicks = 120;

    protected ArrayList<Dot> _dotsOnPlay;

    protected Random _rng;

    protected int _lives;
    protected int _score;
    protected int _level;
    protected int _tickCounter;
    protected int _leftToAdvanceLevel;

    protected float _scoreMultiplier;

    protected boolean _paused;

    public SinglePlayerController(int level, int lives) {
        _lives = lives;
        _level = level;

        _score = 0;
        _tickCounter = _baseTicks;

        _leftToAdvanceLevel = _baseAdvanceLevel;
        _scoreMultiplier = 1.0f;

        _dotsOnPlay = new ArrayList<Dot>();
        _rng = new Random();
    }

    public void performTick() {
        if (_lives <= 0)
            return;

        if (_tickCounter > 0)
            _tickCounter--;
        else {
            Dot dot = new Dot(_rng.nextInt((int) _game.camera.viewportWidth), _rng.nextInt((int) _game.camera.viewportHeight), _rng.nextInt(75) + (50 / _level + 10));

            while (!dot.validate())
                dot = new Dot(_rng.nextInt((int) _game.camera.viewportWidth), _rng.nextInt((int) _game.camera.viewportHeight), _rng.nextInt(75) + (50 / _level + 10));

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
    }

    public int performTouch(int xCoordinate, int yCoordinate) {
        boolean correct = false;

        int ret = 0;

        Iterator<Dot> iter = _dotsOnPlay.iterator();

        while (iter.hasNext()) {
            Dot dot = iter.next();

            if (dot.didTouch(xCoordinate, (int) (_game.camera.viewportHeight - yCoordinate))) {
                correct = true;

                ret = (int)(dot.getScore() * _scoreMultiplier);

                _score += ret;

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

        return ret;
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
