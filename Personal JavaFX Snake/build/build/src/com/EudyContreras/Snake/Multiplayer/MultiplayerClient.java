package com.EudyContreras.Snake.Multiplayer;

import java.util.Observable;

public class MultiplayerClient extends Observable {

	/**
	 * allow the player to open a multiplayer menu, in the menu he will see
	 * users currently online and also be able to see if the users are currently
	 * playing or not and if not then the player can send a play request. the
	 * play request reaches the second client while the requesting client awaits
	 * for a response inside a waiting window. if a response is not received in
	 * within 1 minute then the request sender will be taken out of the await
	 * response window and a message saying "the request has timed out" will
	 * appear. if the client receiving the request accepts the request he will
	 * then enter a window which will allow client to press a button confirming
	 * they are ready to start and if the other player presses the ready to
	 * start button the game will then start
	 *
	 *
	 * all actions performed by each client will be send to the server which
	 * will then use this commands in order to control the rival snake on the
	 * screen of each client. the commands will constantly flow through the
	 * buffer. all the actions and events will be send as commands through the
	 * server.
	 *
	 * the server will also take care of things that are randomly generated
	 * allow this items to be generated symmetrically on both sides and at
	 * parallel times.
	 *
	 */
	public MultiplayerClient(String IP_Address, int port, String userName) {

	}

}
