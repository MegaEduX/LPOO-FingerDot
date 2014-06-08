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

    /**
     * Creates a single player game controller.
     *
     * @param level The level to start on.
     * @param lives The lives to start with.
     */

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

    /**
     * Perform a game tick.
     */

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

    /**
     * Perform a touch.
     *
     * @param xCoordinate The X coordinate of the touch.
     * @param yCoordinate The Y coordinate of the touch.
     * @return The score acquired with the touch.
     */

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

    /**
     * Getter for the number of lives.
     *
     * @return The number of lives.
     */

    public int getLives() {
        return _lives;
    }

    /**
     * Getter for the game score.
     *
     * @return The game score.
     */

    public int getScore() {
        return _score;
    }

    /**
     * Getter for the current game level.
     *
     * @return The current game level.
     */

    public int getLevel() {
        return _level;
    }

    /**
     * Increase the number of lives.
     */

    public void increaseLife() {
        _lives++;
    }

    /**
     * Decrease the number of lives.
     */

    public void decreaseLife() {
        _lives--;
    }

    /**
     * Setter for the number of lives.
     *
     * @param lives The new number of lives.
     */

    public void setLives(int lives) {
        _lives = lives;
    }

    /**
     * Increase the game score.
     *
     * @param score The number to increase the score by.
     */

    public void increaseScore(int score) {
        _score += score;
    }

    /**
     * Decrease the game score.
     *
     * @param score The number to decrease the score by.
     */

    public void decreaseScore(int score) {
        _score -= score;
    }

    /**
     * Set the game score.
     *
     * @param score The number to set the score to.
     */

    public void setScore(int score) {
        _score = score;
    }

    /**
     * Increase the game level.
     */

    public void increaseLevel() {
        _level++;

        _lives++;

        _scoreMultiplier += 0.5f;

        _leftToAdvanceLevel = _baseAdvanceLevel * _level;
    }

    /**
     * Decrease the game level.
     */

    public void decreaseLevel() {
        _level--;

        _scoreMultiplier -= 0.5f;

        _leftToAdvanceLevel = _baseAdvanceLevel * _level;
    }

    /**
     * Getter for the list of dots on play.
     *
     * @return The list of dots on play.
     */

    public ArrayList<Dot> getDotsOnPlay() {
        return _dotsOnPlay;
    }
}
