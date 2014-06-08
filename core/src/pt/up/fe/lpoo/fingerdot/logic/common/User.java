//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.common;

import java.io.Serializable;

public class User implements Serializable {
    private String _username = null;
    private String _pin = null;

    /**
     * Initializes an user.
     *
     * @param un User's username.
     * @param pw User's password.
     */

    public User(String un, String pw) {
        _username = un;
        _pin = pw;
    }

    /**
     * Getter for the user's username.
     *
     * @return The user's username.
     */

    public String getUsername() {
        return _username;
    }

    /**
     * Getter for the user's PIN.
     *
     * @return The user's PIN.
     */

    public String getPin() {
        return _pin;
    }

    /**
     * Setter for the user's username.
     *
     * @param un The user's username.
     */

    public void setUsername(String un) {
        _username = un;
    }

    /**
     * Setter for the user's PIN.
     *
     * @param pw The user's PIN.
     */

    public void setPin(String pw) {
        _pin = pw;
    }
}
