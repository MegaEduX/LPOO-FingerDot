package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import java.util.Comparator;

/**
 * fingerdot
 * <p/>
 * Created by MegaEduX on 26/05/14.
 */

public class LeaderboardEntryComparator implements Comparator<LeaderboardEntry> {
    public int compare(LeaderboardEntry entry1, LeaderboardEntry entry2) {
        if (entry1.score > entry2.score)
            return -1;

        if (entry1.score < entry2.score)
            return 1;

        return 0;
    }
}
