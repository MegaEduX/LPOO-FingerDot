package pt.up.fe.lpoo.fingerdot.logic.common;

/**
 * Created by MegaEduX on 03/06/14.
 */
public class User {
    private String _username = null;
    private String _pin = null;

    public User(String un, String pw) {
        _username = un;
        _pin = pw;
    }

    public String getUsername() {
        return _username;
    }

    public String getPin() {
        return _pin;
    }

    public void setUsername(String un) {
        _username = un;
    }

    public void setPin(String pw) {
        _pin = pw;
    }
}
