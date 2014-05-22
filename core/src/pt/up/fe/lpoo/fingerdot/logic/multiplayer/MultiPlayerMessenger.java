/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.*;

import org.json.JSONObject;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerMatchmakingScreen;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerScreen;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiPlayerMessenger implements WarpListener {
    private MultiPlayerScreen _mpScreen;
    private MultiPlayerMatchmakingScreen _mpmScreen;

    private static String AppWarpAppKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
    private static String AppWarpSecretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";

    private static final int kMaxDotsPerChatMessage = 8;

    public void start() {
        WarpClient.initialize(AppWarpAppKey, AppWarpSecretKey);

        WarpController.getInstance().setListener(this);

        WarpController.getInstance().startAppWarp("username0");
    }

    public void stop() {

    }

    public void setMultiPlayerScreen(MultiPlayerScreen mps) {
        _mpScreen = mps;
    }

    public void setMultiPlayerMatchmakingScreen(MultiPlayerMatchmakingScreen mpms) {
        _mpmScreen = mpms;
    }

    public void broadcastTouch(int x, int y, float points, int radius, boolean correct) {
        try {
            JSONObject data = new JSONObject();

            data.put("x", x);
            data.put("y", y);
            data.put("points", points);
            data.put("radius", radius);
            data.put("correct", correct);

            WarpController.getInstance().sendGameUpdate(data.toString());
        } catch (Exception e) {
            // exception in sendLocation
        }
    }

    public void onGameStarted(String message) {
        if (WarpController.getInstance().isRoomOwner) {
            //  Initializing game with 8 * 15 (120) dots...

            for (int i = 0; i < 15; i++) {
                HashMap<String, ArrayList<Dot>> game = new HashMap<String, ArrayList<Dot>>();

                game.put("dots", GameGenerator.generateGameWithDots(kMaxDotsPerChatMessage));

                try {
                    Gson gs = new Gson();
                    JSONObject data = new JSONObject();

                    String gameStr = gs.toJson(game);

                    System.out.println("Sending dots info: " + gameStr);

                    WarpController.getInstance().sendGameUpdate(gameStr);
                } catch (Exception exc) {

                }
            }
        }

        _mpmScreen.startGame();
    }

    public void onError(String message) {

    }

    public void onGameFinished(int code, boolean isRemote) {

    }

    public void onWaitingStarted(String message) {

    }

    public void onGameUpdateReceived(String message) {
        //  System.out.println("Received message: " + message);

        try {
            JSONObject data = new JSONObject(message);

            if (data.getString("dots") != null && data.getString("dots") != "") {
                //  Both devices, not only the one acting as client, should use this data.
                //  Less lag, more fun? That. :)

                System.out.println("Got dots info! This, to be exact: " + data.getString("dots"));

                Gson gs = new Gson();

                Type listType = new TypeToken<ArrayList<Dot>>() {}.getType();

                ArrayList<Dot> d = gs.fromJson(data.getString("dots"), listType);

                _mpScreen.getController().addDots(d);
            } else {
                int x = data.getInt("x");
                int y = data.getInt("y");
                int radius = data.getInt("radius");

                float points = (float)data.getDouble("points");
                boolean correct = data.getBoolean("correct");

                //  _mpScreen.renderEnemyTouch(x, y, correct);

                _mpScreen.getController().addOpponentDot(new OpponentDot(x, y, radius, correct));

                if (correct)
                    _mpScreen.getController().addOpponentScore((int) points);
                else
                    _mpScreen.getController().removeOpponentLife();

                //  Points somewhere!
            }
        } catch (Exception e) {
            // exception
        }
    }
}
