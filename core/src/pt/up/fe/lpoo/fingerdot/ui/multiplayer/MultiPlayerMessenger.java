/**
 * FingerDot
 * <p/>
 * Created by MegaEduX on 06/05/14.
 */

package pt.up.fe.lpoo.fingerdot.ui.multiplayer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.up.fe.lpoo.fingerdot.logic.common.Dot;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.GameGenerator;
import pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp.*;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
        if (WarpController.getInstance().isRoomOwner) {
            ArrayList<Dot> game = GameGenerator.generateGameWithDots(100);

            try {
                Gson gs = new Gson();
                JSONObject data = new JSONObject();

                gs.toJson(game);
                data.put("dots", data);

                WarpController.getInstance().sendGameUpdate(data.toString());
            } catch (Exception exc) {

            }

        }
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

            if (data.getString("dots") != null && data.getString("dots") != "") {
                Gson gs = new Gson();

                Type listType = new TypeToken<ArrayList<Dot>>() {}.getType();

                ArrayList<Dot> d = gs.fromJson(data.getString("dots"), listType);

                _renderer.getController().setDots(d);
            } else {
                float x = (float)data.getDouble("x");
                float y = (float)data.getDouble("y");
                float points = (float)data.getDouble("points");
                boolean correct = data.getBoolean("correct");

                _renderer.renderEnemyTouch(x, y, correct);

                if (correct)
                    _renderer.getController().addOpponentScore((int) points);
                else
                    _renderer.getController().removeOpponentLife();

                //  Points somewhere!
            }
        } catch (Exception e) {
            // exception
        }
    }



}
