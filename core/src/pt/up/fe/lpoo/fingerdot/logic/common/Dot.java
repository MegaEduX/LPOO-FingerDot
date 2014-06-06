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

    public Dot() {

    }

    public Dot(int x, int y, int radius) {
        _x = x;
        _y = y;
        _radius = radius;
    }

    public boolean didTouch(int x, int y) {
        return Math.pow((x - _x), 2) + Math.pow((y - _y), 2) < Math.pow(_radius, 2);
    }

    public boolean validate() {
        return (_y - _radius > 150);
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public int getRadius() {
        return _radius;
    }

    public int getTicks() {
        return _ticks;
    }

    public void decreaseTicks() {
        _ticks--;
    }

    public int getScore() {
        return _radius * _ticks;
    }

    public int getBaseScore() {
        return _radius * 100;
    }
}
