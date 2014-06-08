//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.test;

import org.junit.*;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardEntry;
import pt.up.fe.lpoo.fingerdot.logic.singleplayer.LeaderboardManager;

import static org.junit.Assert.*;

public class LeaderboardTests {
    LeaderboardManager _manager = LeaderboardManager.sharedManager();

    @Before public void setUp() throws Exception {

    }

    /**
     * Tests whether the game can read the internet leaderboards.
     */

    @Test public void readInternetHighScores() {
        assertTrue(_manager.retrieveOnlineLeaderboard());
    }

    /**
     * Tests whether the game can add a score to the internet leaderboards.
     */

    @Test public void addInternetHighScore() {
        assertTrue(_manager.publishScoreOnOnlineLeaderboard(new LeaderboardEntry("Test Suite", "0", "test-suite", 1)));
    }
}