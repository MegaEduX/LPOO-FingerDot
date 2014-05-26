package pt.up.fe.lpoo.fingerdot.logic.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by MegaEduX on 23/05/14.
 */

public class LeaderboardController {
    private static final String baseApiURL = "https://edr.io/fingerdot/v1/leaderboard/";
    private static final String leaderboardURL = baseApiURL + "get_leaderboard.php";
    private static final String postScoreURL = baseApiURL + "add_score.php";

    private ArrayList<LeaderboardEntry> _onlineLeaderboard = null;
    private ArrayList<LeaderboardEntry> _localLeaderboard = null;

    private LeaderboardEntryComparator lec = new LeaderboardEntryComparator();

    public LeaderboardController() {
        loadLocalLeaderboard();
    }

    public void addLocalScore(LeaderboardEntry entry) {
        _localLeaderboard.add(entry);

        Collections.sort(_localLeaderboard, lec);

        saveLocalLeaderboard();
    }

    public boolean loadLocalLeaderboard() {
        FileHandle file = Gdx.files.local("local_highscores.fdh");

        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(file.read());

                _localLeaderboard = (ArrayList<LeaderboardEntry>) ois.readObject();

                ois.close();

                return true;
            } catch (Exception e) {

            }
        }

        _localLeaderboard = new ArrayList<LeaderboardEntry>();

        return false;
    }

    public boolean saveLocalLeaderboard() {
        FileHandle file = Gdx.files.local("local_highscores.fdh");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(file.write(false));

            oos.writeObject(_localLeaderboard);

            return true;
        } catch (Exception e) {

        }

        return false;
    }

    public boolean getOnlineLeaderboard() {
        try {
            URL lbURL = new URL(leaderboardURL);

            HttpsURLConnection connection = (HttpsURLConnection) lbURL.openConnection();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                Gson gs = new Gson();

                _onlineLeaderboard = gs.fromJson(sb.toString(), (new ArrayList<LeaderboardEntry>()).getClass());
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean publishScoreOnOnlineLeaderboard(String username, int score, String version) {
        try {
            URL lbURL = new URL(postScoreURL + "?username=" + username + "&score=" + score + "&version=" + version);

            HttpsURLConnection connection = (HttpsURLConnection) lbURL.openConnection();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = rd.readLine()) != null)
                    sb.append(line);

                Gson gs = new Gson();

                HashMap<String, Boolean> response = gs.fromJson(sb.toString(), (new HashMap<String, Boolean>()).getClass());

                return response.get("success");
            } catch (Exception e) {
                return false;
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
