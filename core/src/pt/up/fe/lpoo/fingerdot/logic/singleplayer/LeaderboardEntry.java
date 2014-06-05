package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import java.io.Serializable;

/**
 * fingerdot
 * <p/>
 * Created by MegaEduX on 26/05/14.
 */

public class LeaderboardEntry implements Serializable {
    public String username;
    public String timestamp;
    public String version;

    public int score;

    public LeaderboardEntry() {

    }

    public LeaderboardEntry(String n, String t, String v, int s) {
        username = n;
        timestamp = t;
        version = v;
        score = s;
    }
}
