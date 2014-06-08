//
//  FingerDot
//
//  Created by Eduardo Almeida and Joao Almeida
//  LPOO 13/14
//

package pt.up.fe.lpoo.fingerdot.logic.multiplayer.appwarp;

import java.util.HashMap;

import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import pt.up.fe.lpoo.fingerdot.logic.common.UserManager;

public class WarpController {
	private static WarpController instance;
	
	private boolean showLog = true;
	
	private final String apiKey = "3f2e8cbf9a78b7149eea3b4b83b494b07a848178ce913f9525dd528b208c00a4";
	private final String secretKey = "73aa452ec1b4c19dbe089e1411787532206633ad655c3c80ddaf1cd590fe224f";
	
	private WarpClient warpClient;
	
	private String _localUser;
	private String _roomId;
	
	private boolean _connected = false;
	
	private WarpListener warpListener;
	
	private int STATE;
	
	// Game state constants
	public static final int kWaitingForPlayers = 1;
	public static final int kGameStarted = 2;
	public static final int kGameComplete = 3;
	public static final int kGameFinished = 4;
	
	// Game completed constants
	public static final int kGameCompleteWon = 5;
	public static final int kGameCompleteLost = 6;
	public static final int kGameCompleteEnemyLeft = 7;

    public boolean isRoomOwner = false;
	
	private WarpController() {
        try {
            WarpClient.initialize(apiKey, secretKey);
            warpClient = WarpClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
	}

    /**
     * Returns the shared instance of the singleton.
     *
     * @return The object's shared instance.
     */
	
	public static WarpController getInstance() {
		if (instance == null) {
			instance = new WarpController();
		}

		return instance;
	}

    /**
     * Starts the multiplayer session.
     *
     * @param localUser Username to start the session with.
     */
	
	public void startAppWarp(String localUser) {
		this._localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}

    /**
     * Setter for the controller's listener.
     *
     * @param listener The controller's listener.
     */
	
	public void setListener(WarpListener listener){
		this.warpListener = listener;
	}

    /**
     * Stops the multiplayer session.
     */

	public void stopAppWarp() {
		if (_connected) {
			warpClient.unsubscribeRoom(_roomId);
			warpClient.leaveRoom(_roomId);
		}

		warpClient.disconnect();
	}

    /**
     * Sends a game update.
     *
     * @param msg A game update.
     */
	
	public void sendGameUpdate(String msg){
		if (_connected)
		    warpClient.sendUpdatePeers((_localUser + "#@" + msg).getBytes());
	}

    /**
     * Called when a connection is established.
     *
     * @param status The connection's status.
     */
	
	public void onConnectDone(boolean status){
		log("onConnectDone: "+status);

		if (status)
			warpClient.joinRoomInRange(1, 1, false);
		else {
			_connected = false;
			handleError();
		}

        warpListener.onConnectDone(status);
	}

    /**
     * Called when a chat room is created.
     *
     * @param roomId The chat room identifier.
     */
	
	public void onRoomCreated(String roomId){
		if (roomId != null) {
			warpClient.joinRoom(roomId);
		} else {
			handleError();
		}
	}

    /**
     * Called when a chat room is joined.
     *
     * @param event The join event.
     */
	
	public void onJoinRoomDone(RoomEvent event){
		log("onJoinRoomDone: "+event.getResult());

		if (event.getResult() == WarpResponseResultCode.SUCCESS) { // success case
			this._roomId = event.getData().getId();
			warpClient.subscribeRoom(_roomId);
		} else if (event.getResult()==WarpResponseResultCode.RESOURCE_NOT_FOUND) {// no such room found
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			warpClient.createRoom(UserManager.sharedManager().getUser().getUsername(), _localUser, 2, data);

            isRoomOwner = true;
		} else {
			warpClient.disconnect();
			handleError();
		}
	}

    /**
     * Called when a chat room is subscribed.
     *
     * @param roomId The chat room identifier.
     */
	
	public void onRoomSubscribed(String roomId) {
		log("onSubscribeRoomDone: " + roomId);

		if (roomId != null) {
			_connected = true;
			warpClient.getLiveRoomInfo(roomId);
		} else {
			warpClient.disconnect();
			handleError();
		}
	}

    /**
     * Called when the users of a room are returned.
     *
     * @param liveUsers The users on a chat room.
     */
	
	public void onGetLiveRoomInfo(String[] liveUsers){
		log("onGetLiveRoomInfo: "+liveUsers.length);

        for (String user : liveUsers) {
            log(user);
        }

		if (liveUsers != null) {
			if (liveUsers.length == 2){
				startGame();	
			} else {
				waitForOtherUser();
			}
		} else {
			warpClient.disconnect();
			handleError();
		}
	}

    /**
     * Called when a user joins the chat room the user is on.
     *
     * @param roomId The chat room identifier.
     * @param userName The username of the new user.
     */
	
	public void onUserJoinedRoom(String roomId, String userName){
		/*
		 * if room id is same and username is different then start the game
		 */

		if (_localUser.equals(userName) == false) {
			startGame();
		}
	}

    /**
     * Called when a message is sent to the chat room.
     *
     * @param status true on success, false if otherwise.
     */

	public void onSendChatDone(boolean status){
		log("onSendChatDone: " + status);
	}

    /**
     * Called when a game update is received.
     *
     * @param message The game update.
     */
	
	public void onGameUpdateReceived(String message){
        System.out.println("Received Game Update: " + message);

		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@") + 2, message.length());

		if (!_localUser.equals(userName) || data.contains("{\"_gd\":[{"))
			warpListener.onGameUpdateReceived(data);
	}

    /**
     * Called when the status of the game changes.
     *
     * @param userName The user that sent the status.
     * @param code The status code.
     */

	public void onResultUpdateReceived(String userName, int code){
		if (_localUser.equals(userName) == false) {
			STATE = kGameFinished;
			warpListener.onGameFinished(code, true);
		} else {
			warpListener.onGameFinished(code, false);
		}
	}

    /**
     * Called when a user leaves the game.
     *
     * @param roomId The id of the room.
     * @param userName The username of the user.
     */
	
	public void onUserLeftRoom(String roomId, String userName) {
        log("onUserLeftRoom " + userName + " in room " + roomId);

        if (STATE == kGameStarted && !_localUser.equals(userName)) { // Game Started and other user left the room
            warpListener.onGameFinished(kGameCompleteEnemyLeft, true);
        }
    }

    /**
     * Logs a message to the console.
     *
     * @param message The message to log.
     */

	private void log(String message){
		if (showLog) {
			System.out.println(message);
		}
	}

    /**
     * Alert the listener to start a multiplayer game.
     */
	
	private void startGame() {
		STATE = kGameStarted;
		warpListener.onGameStarted("Start the Game");
	}

    /**
     * Alert the listener that we are currently waiting for another user to join our game session.
     */
	
	private void waitForOtherUser() {
		STATE = kWaitingForPlayers;
		warpListener.onWaitingStarted("Waiting for other user");
	}

    /**
     * Handle a server error.
     */
	
	private void handleError(){
		if (_roomId != null && _roomId.length() > 0) {
			warpClient.deleteRoom(_roomId);
		}

		disconnect();
	}

    /**
     * Disconnect from the server.
     */
	
	private void disconnect(){
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));

		warpClient.disconnect();
	}
}
