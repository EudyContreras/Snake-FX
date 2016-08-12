package com.EudyContreras.Snake.MultiplayerServer;

import java.util.HashMap;

import com.EudyContreras.Snake.DataPackage.SessionDetails;
import com.EudyContreras.Snake.MultiplayerServer.MultiplayerClient.Status;

/**
 * This class is used to handle clients and all actions from each client
 * this class organizes the logic used to send objects to specific clients
 * and it also handles the information that travels through the server.
 * @author Eudy Contreras, Mikael Malmgren, Johannes Berggren
 *
 */
public class SessionManager extends MultiplayerServer{

	private HashMap<GameSession,Long> sessions;
	private ClientManager clientManager;
	private static long SESSION_ID = 0;
	/**
	 * Constructor which takes the GUI as an argument.
	 * @param GUI the servers graphical user interface.
	 */
	public SessionManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}
	public void createGameSession(MultiplayerClient playerOne, MultiplayerClient playerTwo, SessionDetails sessionDetails) {
		SESSION_ID++;
		playerOne.setStatus(Status.IN_MATCH);
		playerTwo.setStatus(Status.IN_MATCH);
		sessions.put(new GameSession(sessionDetails,playerOne,playerTwo, SESSION_ID), SESSION_ID);

	}
}