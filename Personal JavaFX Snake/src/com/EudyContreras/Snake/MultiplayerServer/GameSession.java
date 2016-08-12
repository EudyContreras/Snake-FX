package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;

import com.EudyContreras.Snake.Commands.ClientCommand;
import com.EudyContreras.Snake.DataPackage.ClientEvent;
import com.EudyContreras.Snake.DataPackage.GameEvent;
import com.EudyContreras.Snake.DataPackage.GameStatus;
import com.EudyContreras.Snake.DataPackage.MatchRequest;
import com.EudyContreras.Snake.DataPackage.MatchStatus;
import com.EudyContreras.Snake.DataPackage.PlayerAction;
import com.EudyContreras.Snake.DataPackage.SessionDetails;

public class GameSession {
	private int sessionID;
	private SessionDetails details;
	private MultiplayerClient playerOne;
	private MultiplayerClient playerTwo;
	private Status sessionStatus;


	public GameSession(SessionDetails details, MultiplayerClient playerOne, MultiplayerClient playerTwo, long sessionID){
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.details = details;
	}
	/**
	 * This method checks the objects received by the clients and then
	 * determines the type of objects. Once it is done it sends it forward for processing
	 * @param object
	 * @param client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public  void handleObject(Object object, MultiplayerClient client){
			 if(object instanceof GameEvent){handleGameEvent((GameEvent)object, client);}
		else if(object instanceof ClientEvent){handleClientEvent((ClientEvent)object, client);}
		else if(object instanceof MatchRequest){HandleMatchRequest((MatchRequest)object, client);}
		else if(object instanceof MatchStatus){HandleMatchStatus((MatchStatus)object, client);}
		else if(object instanceof PlayerAction){HandlePlayerAction((PlayerAction)object, client);}
		else if(object instanceof GameStatus){HandleStatusUpdate((GameStatus)object, client);}

	}
	private void HandleStatusUpdate(GameStatus obj, MultiplayerClient client) {
		// TODO Auto-generated method stub

	}

	private void HandlePlayerAction(PlayerAction obj, MultiplayerClient client) {


	}

	private void HandleMatchStatus(MatchStatus obj, MultiplayerClient client) {
		// TODO Auto-generated method stub

	}

	private void HandleMatchRequest(MatchRequest obj, MultiplayerClient client) {
		// TODO Auto-generated method stub

	}

	private void handleGameEvent(GameEvent obj, MultiplayerClient client) {
		// TODO Auto-generated method stub

	}

	private void handleClientEvent(ClientEvent obj, MultiplayerClient client) {
		// TODO Auto-generated method stub

	}
	private void handleClientCommand(ClientCommand command){

		switch(command){
		case DISCONNECT:
			break;
		case RESEND:
			break;
		default:
			break;

		}
	}
	public enum Status{
		ACTIVE,
		INNACTIVE,
		TERMINATED,
	}
}
