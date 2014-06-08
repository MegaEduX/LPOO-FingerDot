//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;

import java.util.ArrayList;
import java.util.Random;

public class GameGenerator {
    static protected int _baseAdvanceLevel = 20;

    /**
     * Generates a game with a given number of dots.
     *
     * @param dotsNumber Game dots.
     * @return A list of dots to be played.
     */

    static public ArrayList<Dot> generateGameWithDots(int dotsNumber) {
        if (dotsNumber < 1)
            return null;

        int level = 1;
        int toAdvance = _baseAdvanceLevel;

        final FingerDot game = FingerDot.getSharedInstance();

        Random rng = new Random();;

        ArrayList<Dot> ret = new ArrayList<Dot>();

        for (int i = 0; i < dotsNumber; i++) {

            int width, height;

            try {
                width = (int)game.camera.viewportWidth;
                height = (int)game.camera.viewportHeight;
            } catch (NullPointerException exc) {
                width = 1280;
                height = 720;
            }

            Dot dot = new Dot(rng.nextInt(width), rng.nextInt(height - 120) + 120, rng.nextInt(75) + (50 / level + 10));

            toAdvance--;

            if (toAdvance == 0)
                level++;

            ret.add(dot);
        }

        return ret;
    }

    /**
     * Generates a part of a game.
     *
     * @param dotsNumber Number of dots in the part.
     * @param part Part number.
     * @param parts Number of parts.
     * @return A game part.
     */

    static public GameGeneratorPart generateGamePartWithDots(int dotsNumber, int part, int parts) {
        return new GameGeneratorPart(generateGameWithDots(dotsNumber), part, parts);
    }
}
