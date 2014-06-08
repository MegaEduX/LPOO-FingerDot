//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.common.FingerDot;
import pt.up.fe.lpoo.fingerdot.logic.common.OpponentDot;
import pt.up.fe.lpoo.fingerdot.logic.common.UserManager;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.*;

import org.json.JSONObject;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerMatchmakingScreen;
import pt.up.fe.lpoo.fingerdot.ui.multiplayer.MultiPlayerScreen;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MultiPlayerMessenger implements WarpListener {
    private class GameOverMessage {
        public boolean gameOver;

        public int points;
        public int dotsLeft;

        public MultiPlayerController.GameState expectedGameState;

        public GameOverMessage(int p, int d, MultiPlayerController.GameState g) {
            gameOver = true;

            points = p;
            dotsLeft = d;
            expectedGameState = g;
        }
    }

    private MultiPlayerScreen _mpScreen = null;
    private MultiPlayerMatchmakingScreen _mpmScreen = null;

    private GameBuilder _gameBuilder = null;

    private Gson _gson = null;

    private static String AppWarpAppKey = "14a611b4b3075972be364a7270d9b69a5d2b24898ac483e32d4dc72b2df039ef";
    private static String AppWarpSecretKey = "55216a9a165b08d93f9390435c9be4739888d971a17170591979e5837f618059";

    private static final int kOpponentDotSize = 50;

    private static final int kMaxDotsPerChatMessage = 6;    //  Theoretically this is 8, but let's play safe.

    /**
     * Starts a connection with the MultiPlayer server.
     */

    public void start() {
        WarpClient.initialize(AppWarpAppKey, AppWarpSecretKey);

        WarpController.getInstance().setListener(this);

        _gson = new Gson();

        String username = UserManager.sharedManager().getUser().getUsername();

        System.out.println("Initializing with username \"" + username + "\"...");

        WarpController.getInstance().startAppWarp(username);
    }

    /**
     * Stops a connection with the server.
     */

    public void stop() {
        WarpController.getInstance().stopAppWarp();
    }

    /**
     * Setter for the MultiPlayer Screen.
     * @param mps A MultiPlayerScreen instance.
     */

    public void setMultiPlayerScreen(MultiPlayerScreen mps) {
        _mpScreen = mps;
    }

    /**
     * Setter for the MultiPlayer Matchmaking Screen
     * @param mpms A MultiPlayerMatchmakingScreen instance.
     */

    public void setMultiPlayerMatchmakingScreen(MultiPlayerMatchmakingScreen mpms) {
        _mpmScreen = mpms;
    }

    /**
     * Broadcast a touch.
     *
     * @param x The X coordinate of the touch.
     * @param y The Y coordinate of the touch.
     * @param points The points' worth of the touch.
     * @param correct The "correct-ness" of the touch.
     */

    public void broadcastTouch(int x, int y, float points, boolean correct) {
        try {
            JSONObject data = new JSONObject();

            data.put("x", x);
            data.put("y", FingerDot.getSharedInstance().camera.viewportHeight - y);
            data.put("points", points);
            data.put("correct", correct);

            WarpController.getInstance().sendGameUpdate(data.toString());
        } catch (Exception e) {
            // exception in sendLocation
        }
    }

    /**
     * Broadcast a game end.
     *
     * @param score The score of the user.
     * @param dotsLeft The dots left on the game.
     * @param expectedState The state of the game.
     */

    public void broadcastEndOfGame(int score, int dotsLeft, MultiPlayerController.GameState expectedState) {
        try {
            GameOverMessage message = new GameOverMessage(score, dotsLeft, expectedState);

            WarpController.getInstance().sendGameUpdate(_gson.toJson(message));
        } catch (Exception e) {

        }
    }

    /**
     * Called when the game is started.
     *
     * @param message The message sent along with the call.
     */

    public void onGameStarted(String message) {
        _mpmScreen.setCurrentMessage("Negotiating the game...");

        if (WarpController.getInstance().isRoomOwner) {
            //  Initializing game with 6 * 40 (240) dots... Should be enough for a long game. :P

            final int messages = 40;

            for (int i = 0; i < messages; i++) {
                try {
                    String gameStr = _gson.toJson(GameGenerator.generateGamePartWithDots(kMaxDotsPerChatMessage, i, messages));

                    System.out.println("Sending dots info: " + gameStr);

                    WarpController.getInstance().sendGameUpdate(gameStr);
                } catch (Exception exc) {

                }
            }
        }

        _mpmScreen.startGame();
    }

    /**
     * Called when a connection is established with the server.
     *
     * @param status true on success, false otherwise.
     */

    public void onConnectDone(boolean status) {
        if (status)
            _mpmScreen.setCurrentMessage("Searching for a suitable opponent...");
        else
            _mpmScreen.setCurrentMessage("Connection failed! Please try again later.");
    }

    /**
     * Called when an error has occoured.
     *
     * @param message The error message.
     */

    public void onError(String message) {

    }

    /**
     * Called when a game has finished.
     *
     * @param code The code.
     * @param isRemote Whether the message was sent by a remote client or not.
     */

    public void onGameFinished(int code, boolean isRemote) {

    }

    /**
     * Called when the game starts waiting for an opponent.
     *
     * @param message The message sent along with the call.
     */

    public void onWaitingStarted(String message) {

    }

    /**
     * Called when a list of dots is received.
     *
     * @param dots The list of dots.
     */

    public void onDotsReceived(ArrayList<Dot> dots) {
        _mpScreen.getController().setDots(dots);
    }

    /**
     * Called when a game update is received.
     *
     * @param message The game update.
     */

    public void onGameUpdateReceived(String message) {
        System.out.println("Received message: " + message);

        try {
            JSONObject data = new JSONObject(message);

            if (message.contains("\"_gd\":[")) {
                //  Both devices, not only the one acting as client, should use this data.
                //  Less lag, more fun? That. :)

                System.out.println("Got dots info! This, to be exact: " + data.getString("_gd"));

                Type gameGeneratorPartType = new TypeToken<GameGeneratorPart>(){}.getType();

                GameGeneratorPart d = _gson.fromJson(data.toString(), gameGeneratorPartType);

                if (_gameBuilder == null)
                    _gameBuilder = new GameBuilder(this);

                _gameBuilder.addPart(d);
            } else if (message.contains("\"correct\":")) {
                System.out.println("Got an opponent touch!");

                int x = data.getInt("x");
                int y = data.getInt("y");

                float points = (float)data.getDouble("points");
                boolean correct = data.getBoolean("correct");

                _mpScreen.getController().addOpponentDot(new OpponentDot(x, y, kOpponentDotSize, correct));

                if (correct)
                    _mpScreen.getController().addOpponentScore((int) points);
                else
                    _mpScreen.getController().removeOpponentLife();

                //  Points somewhere!
            } else if (message.contains("\"gameOver\"")) {
                Type gameOverMessageType = new TypeToken<GameOverMessage>(){}.getType();

                GameOverMessage gom = _gson.fromJson(data.toString(), gameOverMessageType);

                switch (gom.expectedGameState) {
                    case GameStateLost:
                        _mpScreen.getController().setGameState(MultiPlayerController.GameState.GameStateWon);

                        break;

                    case GameStateTie:
                        _mpScreen.getController().setGameState(MultiPlayerController.GameState.GameStateTie);

                        break;

                    default:
                        throw new Exception("Invalid Game Type!");
                }
            } else {
                throw new Exception("Unknown Message Received!");
            }
        } catch (Exception e) {
            System.out.println("Exception at onGameUpdateReceived: " + e);
        }
    }
}
