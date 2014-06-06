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
	
	public WarpController() {
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
	
	public static WarpController getInstance() {
		if (instance == null) {
			instance = new WarpController();
		}

		return instance;
	}
	
	public void startAppWarp(String localUser) {
		this._localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}
	
	public void setListener(WarpListener listener){
		this.warpListener = listener;
	}
	
	public void stopAppWarp() {
		if (_connected) {
			warpClient.unsubscribeRoom(_roomId);
			warpClient.leaveRoom(_roomId);
		}

		warpClient.disconnect();
	}
	
	public void sendGameUpdate(String msg){
		if (_connected)
		    warpClient.sendUpdatePeers((_localUser + "#@" + msg).getBytes());
	}
	
	public void updateResult(int code, String msg){
		if (_connected) {
			STATE = kGameComplete;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}
	
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
	
	public void onDisconnectDone(boolean status){
		
	}
	
	public void onRoomCreated(String roomId){
		if (roomId != null) {
			warpClient.joinRoom(roomId);
		} else {
			handleError();
		}
	}
	
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
	
	public void onUserJoinedRoom(String roomId, String userName){
		/*
		 * if room id is same and username is different then start the game
		 */

		if (_localUser.equals(userName) == false) {
			startGame();
		}
	}

	public void onSendChatDone(boolean status){
		log("onSendChatDone: " + status);
	}
	
	public void onGameUpdateReceived(String message){
        System.out.println("Received Game Update: " + message);

		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@") + 2, message.length());

		if (!_localUser.equals(userName) || data.contains("{\"_gd\":[{"))
			warpListener.onGameUpdateReceived(data);
	}
	
	public void onResultUpdateReceived(String userName, int code){
		if (_localUser.equals(userName) == false) {
			STATE = kGameFinished;
			warpListener.onGameFinished(code, true);
		} else {
			warpListener.onGameFinished(code, false);
		}
	}
	
	public void onUserLeftRoom(String roomId, String userName){
		log("onUserLeftRoom "+userName+" in room "+roomId);

		if (STATE == kGameStarted && !_localUser.equals(userName)) { // Game Started and other user left the room
			warpListener.onGameFinished(kGameCompleteEnemyLeft, true);
		}
	}
	
	public int getState(){
		return this.STATE;
	}
	
	private void log(String message){
		if(showLog) {
			System.out.println(message);
		}
	}
	
	private void startGame() {
		STATE = kGameStarted;
		warpListener.onGameStarted("Start the Game");
	}
	
	private void waitForOtherUser(){
		STATE = kWaitingForPlayers;
		warpListener.onWaitingStarted("Waiting for other user");
	}
	
	private void handleError(){
		if (_roomId != null && _roomId.length() > 0) {
			warpClient.deleteRoom(_roomId);
		}

		disconnect();
	}
	
	public void handleLeave(){
		if (_connected) {
			warpClient.unsubscribeRoom(_roomId);
			warpClient.leaveRoom(_roomId);

			if (STATE != kGameStarted){
				warpClient.deleteRoom(_roomId);
			}

			warpClient.disconnect();
		}
	}
	
	private void disconnect(){
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));

		warpClient.disconnect();
	}
}
