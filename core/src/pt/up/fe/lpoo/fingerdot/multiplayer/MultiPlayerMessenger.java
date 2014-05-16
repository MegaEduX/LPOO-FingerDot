/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

package pt.up.fe.lpoo.fingerdot.multiplayer;

import pt.up.fe.lpoo.fingerdot.multiplayer.appwarp.*;

import org.json.JSONObject;

public class MultiPlayerMessenger implements WarpListener {
    final MultiPlayerScreen _renderer;

    public MultiPlayerMessenger(MultiPlayerScreen renderer) {
        _renderer = renderer;
    }

    private void broadcastTouch(float x, float y, float points, boolean correct){
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

    public void onGameStarted(String message) {

    }

    public void onError(String message) {

    }

    public void onGameFinished(int code, boolean isRemote) {

    }

    public void onWaitingStarted(String message) {

    }

    public void onGameUpdateReceived(String message) {
        try {
            JSONObject data = new JSONObject(message);

            float x = (float)data.getDouble("x");
            float y = (float)data.getDouble("y");
            float points = (float)data.getDouble("points");
            boolean correct = (boolean)data.getBoolean("correct");

            _renderer.renderEnemyTouch(x, y, correct);

            //  Points somewhere!
        } catch (Exception e) {
            // exception
        }
    }



}
