//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import java.io.Serializable;

public class LeaderboardEntry implements Serializable {
    public String username;
    public String timestamp;
    public String version;

    public int score;

    /**
     * Initializes an empty leaderboard entry.
     */

    public LeaderboardEntry() {

    }

    /**
     * Initializes a leaderboard entry.
     *
     * @param n The username of the user.
     * @param t The timestamp of the entry.
     * @param v The version of the application.
     * @param s The score.
     */

    public LeaderboardEntry(String n, String t, String v, int s) {
        username = n;
        timestamp = t;
        version = v;
        score = s;
    }
}
