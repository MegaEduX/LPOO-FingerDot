package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

import java.util.ArrayList;
import java.util.Random;

/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 16/05/14.
 */

public class GameGenerator {
    static protected int _baseAdvanceLevel = 20;

    static public ArrayList<Dot> generateGameWithDots(int dotsNumber) {
        if (dotsNumber < 1)
            return null;

        int level = 1;
        int toAdvance = _baseAdvanceLevel;

        final FingerDot game = FingerDot.getSharedInstance();

        Random rng = new Random();;

        ArrayList<Dot> ret = new ArrayList<Dot>();

        for (int i = 0; i < dotsNumber; i++) {
            Dot dot = new Dot(rng.nextInt((int) game.camera.viewportWidth), rng.nextInt((int) game.camera.viewportHeight), rng.nextInt(75) + (50 / level + 10));

            toAdvance--;

            if (toAdvance == 0)
                level++;

            ret.add(dot);
        }

        return ret;
    }

    static public GameGeneratorPart generateGamePartWithDots(int dotsNumber, int part, int parts) {
        return new GameGeneratorPart(generateGameWithDots(dotsNumber), part, parts);
    }
}
