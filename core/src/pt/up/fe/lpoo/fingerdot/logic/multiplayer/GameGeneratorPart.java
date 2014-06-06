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

    public GameGeneratorPart(ArrayList<Dot> object, int part, int length) {
        _gd = object;
        _p = part;
        _l = length;
    }

    public ArrayList<Dot> getDots() {
        return _gd;
    }

    public int getPartNumber() {
        return _p;
    }

    public int getLength() {
        return _l;
    }
}
