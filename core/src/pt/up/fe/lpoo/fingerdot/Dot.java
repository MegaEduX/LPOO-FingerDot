package pt.up.fe.lpoo.fingerdot;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 02/05/14.
 */
public class Dot {
    private int _x;
    private int _y;
    private int _radius;
    private int _ticks = 100;

    public Dot(int x, int y, int radius) {
        _x = x;
        _y = y;
        _radius = radius;
    }

    public boolean didTouch(int x, int y) {
        return Math.pow((x - _x), 2) + Math.pow((y - _y), 2) < Math.pow(_radius, 2);
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

    public void setTicks(int ticks) {
        _ticks = ticks;
    }

    public void decreaseTicks() {
        _ticks--;
    }

    public int getScore() {
        return _radius * _ticks;
    }
}
