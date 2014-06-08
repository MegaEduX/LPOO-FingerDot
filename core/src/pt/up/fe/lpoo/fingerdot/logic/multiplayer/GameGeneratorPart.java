//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;

import java.io.Serializable;
import java.util.ArrayList;

public class GameGeneratorPart implements Serializable {
    ArrayList<Dot> _gd;

    int _p;
    int _l;

    /**
     * Intializes a part of a multiplayer game's dots.
     *
     * @param object The dots included on the part.
     * @param part The part number.
     * @param length The number of dots on the part.
     */

    public GameGeneratorPart(ArrayList<Dot> object, int part, int length) {
        _gd = object;
        _p = part;
        _l = length;
    }

    /**
     * Getter for the dots on the part.
     *
     * @return The dots included on the part.
     */

    public ArrayList<Dot> getDots() {
        return _gd;
    }

    /**
     * Getter for the number of the part.
     *
     * @return The part number.
     */

    public int getPartNumber() {
        return _p;
    }

    /**
     * Getter for the part length.
     *
     * @return The part length.
     */

    public int getLength() {
        return _l;
    }
}
