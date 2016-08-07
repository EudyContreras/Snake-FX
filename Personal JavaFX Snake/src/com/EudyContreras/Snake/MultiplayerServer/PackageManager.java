package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;

import com.EudyContreras.Snake.Commands.ClientCommand;
import com.EudyContreras.Snake.DataPackage.ClientEvent;
import com.EudyContreras.Snake.DataPackage.GameEvent;
import com.EudyContreras.Snake.DataPackage.GameStatus;
import com.EudyContreras.Snake.DataPackage.MatchRequest;
import com.EudyContreras.Snake.DataPackage.MatchStatus;
import com.EudyContreras.Snake.DataPackage.PlayerAction;
import com.EudyContreras.Snake.DataPackage.ServerResponse;

/**
 * This class handles packages sent by the client attached to this class.
 * this class determines what to do and where to redirect the packages for
 * further processing. Some packages may be processed directly by this class
 * for low latency purposes!
 *
 *send to client manager
 *process package
 *if mathch request
 *handle
 *if request accepted create session
 *handle information based on the status of client
 *
 * @author Eudy Contreras
 *
 */
public class PackageManager{

	/**
	 * Constructor which takes the GUI as an argument.
	 * @param GUI the servers graphical user interface.
	 */
	public PackageManager(ClientManager clientManager) {
		super();

	}

	/**
	 * This method checks the objects received by the clients and then
	 * determines the type of objects. Once it is done it sends it forward for processing
	 * @param object
	 * @param client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void handleObject(Object object, MultiplayerClient client){
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

	/**
	 * Method responsible of handling all infopacks that travel through this server
	 * this method will determining both the content and nature of the infopack, this method will
	 * then process or send the infopack further for processing.
	 * @param obj
	 * @param client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void handleInfoPack(Object obj, MultiplayerClient client) throws ClassNotFoundException, IOException {
		ServerResponse pack = (ServerResponse) obj;
		Boolean isImage = pack.getImageStatus();
		String sender = pack.getID();
		String getter = pack.getReceiver();
		String message = pack.getMessage();
		String imageName = pack.getImageName();
		String sms = sender + ": " + message;
		if (pack.getCommand() == null) {

		}
		else if (pack.getCommand() != null) {
			handleSpecialPackage(client, pack);
		}
	}

	public void handleSpecialPackage(MultiplayerClient client, ServerResponse pack){
		if	(pack.getCommand() == 1.0){
		}
		else if(pack.getCommand() == 2.0){
		}
		else if(pack.getCommand() == 3.0){
		}
		else if(pack.getCommand() == 5.0){

		}
		else if(pack.getCommand() == 6.0){

		}
	}

	public void handleUserCommand(Object obj, MultiplayerClient client){
		Double command = (Double) obj;
		Double closingCommand = 1.0;

		if (command == 1.0) {

		}
		else if (command == 2.0) {

		}
		else if (command == 4.0) {

		}
	}
	public void showOnConsole(String event){
		System.out.println("ClientManager: " + event);
	}

}