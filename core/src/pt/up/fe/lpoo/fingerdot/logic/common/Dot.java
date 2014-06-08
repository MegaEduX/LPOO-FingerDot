//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.common;

public class Dot {
    protected int _x;
    protected int _y;
    protected int _radius;
    protected int _ticks = 100;

    /**
     *  Default, empty initializer.
     */

    public Dot() {

    }

    /**
     *  Initializer with arguments.
     *
     *  @param x Coordinate of the x center.
     *  @param y Coordinate of the y center.
     *  @param radius Radius (in pixels).
     */

    public Dot(int x, int y, int radius) {
        _x = x;
        _y = y;
        _radius = radius;
    }

    /**
     * Checks if a touch was done inside the dot.
     *
     * @param x X coordinate of the touch.
     * @param y Y coordinate of the touch.
     * @return true of false, whether the touch was inside or not.
     */

    public boolean didTouch(int x, int y) {
        return Math.pow((x - _x), 2) + Math.pow((y - _y), 2) < Math.pow(_radius, 2);
    }

    /**
     * Validates a dot.
     *
     * @return true if the dot is valid, false if not.
     */

    public boolean validate() {
        return (_y - _radius > 150);
    }

    /**
     * Getter for the X coordinate of the dot.
     *
     * @return The X coordinate of the dot.
     */

    public int getX() {
        return _x;
    }

    /**
     * Getter for the Y coordinate of the dot.
     *
     * @return The Y coordinate of the dot.
     */

    public int getY() {
        return _y;
    }

    /**
     * Getter for the radius of the dot.
     *
     * @return The radius of the dot.
     */

    public int getRadius() {
        return _radius;
    }

    /**
     * Getter for the ticks left on the dot.
     *
     * @return The ticks left on the dot.
     */

    public int getTicks() {
        return _ticks;
    }

    /**
     * Decreases the ticks count by one.
     */

    public void decreaseTicks() {
        _ticks--;
    }

    /**
     * Returns the current dot's worth.
     *
     * @return The dot's worth.
     */

    public int getScore() {
        return _radius * _ticks;
    }

    /**
     * Returns for the base score of the dot.
     *
     * @return The base score of the dot.
     */

    public int getBaseScore() {
        return _radius * 100;
    }
}
