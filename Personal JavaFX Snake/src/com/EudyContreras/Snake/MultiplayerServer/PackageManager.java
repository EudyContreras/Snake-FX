package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;

import com.EudyContreras.Snake.DataPackage.ServerResponse;

/**
 * This class is used to handle clients and all actions from each client
 * this class organizes the logic used to send objects to specific clients
 * and it also handles the information that travels through the server.
 * @author Eudy Contreras, Mikael Malmgren, Johannes Berggren
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
	 * @param obj
	 * @param client
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public synchronized void handleObject(Object obj, MultiplayerClient client) throws ClassNotFoundException, IOException{
			 if(obj instanceof ServerResponse){handleInfoPack(obj, client);}
		else if(obj instanceof Double){handleUserCommand(obj, client);}

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