//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import java.util.Comparator;

public class LeaderboardEntryComparator implements Comparator<LeaderboardEntry> {

    /**
     * Compares two leaderboard entries.
     *
     * @param entry1 A leaderboard entry.
     * @param entry2 Another leaderboard entry.
     * @return -1 if entry1 > entry2, 1 if entry1 < entry2, 0 if entry1 == entry2.
     */

    public int compare(LeaderboardEntry entry1, LeaderboardEntry entry2) {
        if (entry1.score > entry2.score)
            return -1;

        if (entry1.score < entry2.score)
            return 1;

        return 0;
    }
}
