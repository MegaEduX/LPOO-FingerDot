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
import java.util.Random;

public class MultiPlayerMessenger implements WarpListener {
    private MultiPlayerScreen _mpScreen = null;
    private MultiPlayerMatchmakingScreen _mpmScreen = null;

    private GameBuilder _gameBuilder = null;

    private static String AppWarpAppKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
    private static String AppWarpSecretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";

    private static final int kMaxDotsPerChatMessage = 6;    //  Theoretically this is 8, but let's play safe.

    public void start() {
        WarpClient.initialize(AppWarpAppKey, AppWarpSecretKey);

        WarpController.getInstance().setListener(this);

        Random rand = new Random();

        String username = "username" + rand.nextInt(5000) + 10;

        System.out.println("Initializing with username \"" + username + "\"...");

        WarpController.getInstance().startAppWarp(username);
    }

    public void stop() {

    }

    public void setMultiPlayerScreen(MultiPlayerScreen mps) {
        _mpScreen = mps;
    }

    public void setMultiPlayerMatchmakingScreen(MultiPlayerMatchmakingScreen mpms) {
        _mpmScreen = mpms;
    }

    public void broadcastTouch(int x, int y, float points, boolean correct) {
        try {
            JSONObject data = new JSONObject();

            data.put("x", x);
            data.put("y", y);
            data.put("points", points);
            data.put("correct", correct);

            WarpController.getInstance().sendGameUpdate(data.toString());
        } catch (Exception e) {
            // exception in sendLocation
        }
    }

    public void broadcastLoss() {
        try {
            JSONObject data = new JSONObject();

            data.put("gameOver", "noLives");

            WarpController.getInstance().sendGameUpdate(data.toString());
        } catch (Exception e) {

        }
    }

    public void broadcastEndOfGame(int score) {
        try {
            JSONObject data = new JSONObject();

            data.put("gameOver", "endOfGame");
            data.put("points", score);

            WarpController.getInstance().sendGameUpdate(data.toString());
        } catch (Exception e) {

        }
    }

    public void onGameStarted(String message) {
        if (WarpController.getInstance().isRoomOwner) {
            //  Initializing game with 6 * 40 (240) dots... Should be enough for a long game. :P

            final int messages = 40;

            for (int i = 0; i < messages; i++) {
                try {
                    Gson gs = new Gson();

                    String gameStr = gs.toJson(GameGenerator.generateGamePartWithDots(kMaxDotsPerChatMessage, i, messages));

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

    public void onDotsReceived(ArrayList<Dot> dots) {
        _mpScreen.getController().setDots(dots);
    }

    public void onGameUpdateReceived(String message) {
        System.out.println("Received message: " + message);

        try {
            JSONObject data = new JSONObject(message);

            if (message.contains("{\"_gd\":[{\"")) {
                //  Both devices, not only the one acting as client, should use this data.
                //  Less lag, more fun? That. :)

                System.out.println("Got dots info! This, to be exact: " + data.getString("_gd"));

                Gson gs = new Gson();

                Type gameGeneratorPartType = new TypeToken<GameGeneratorPart>(){}.getType();

                GameGeneratorPart d = gs.fromJson(data.toString(), gameGeneratorPartType);

                //  _mpScreen.getController().addDots(d.getDots());

                if (_gameBuilder == null)
                    _gameBuilder = new GameBuilder(this);

                _gameBuilder.addPart(d);
            } else {
                int x = data.getInt("x");
                int y = data.getInt("y");
                int radius = data.getInt("radius");

                float points = (float)data.getDouble("points");
                boolean correct = data.getBoolean("correct");

                _mpScreen.getController().addOpponentDot(new OpponentDot(x, y, radius / 2, correct));

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
