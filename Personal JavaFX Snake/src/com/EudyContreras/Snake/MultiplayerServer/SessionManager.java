package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.EudyContreras.Snake.DataPackage.InfoPack;

/**
 * This class is used to handle clients and all actions from each client
 * this class organizes the logic used to send objects to specific clients
 * and it also handles the information that travels through the server.
 * @author Eudy Contreras, Mikael Malmgren, Johannes Berggren
 *
 */
public class SessionManager extends MultiplayerServer{

	private HashMap<String, MultiplayerClient> all_clients;
	private HashMap<String, MultiplayerClient> online_clients;
	private HashMap<String, MultiplayerClient> offline_clients;
	private HashMap<String, String> online_user_info;
	private HashMap<String, String> offline_user_info;

	private ClientAuthentification authentification;
	private ServerGUI GUI;
	/**
	 * Constructor which takes the GUI as an argument.
	 * @param GUI the servers graphical user interface.
	 */
	public SessionManager(ServerGUI GUI) {
		super();
		this.GUI = GUI;
		all_clients = new HashMap<>();
		online_clients = new HashMap<>();
		offline_clients = new HashMap<>();
		online_user_info = new HashMap<>();
		offline_user_info = new HashMap<>();
		authentification = new ClientAuthentification(this);
	}
	/**
	 * Receives a new connecting client and sends it
	 * to be revised for uniqueness.
	 * @param client
	 */
	public synchronized void addIncominglient(MultiplayerClient client) {
		authentification.performInstanceCheck(client);
	}
	/**
	 * Receives the first client that connects to the server
	 * @param client
	 */

	public HashMap<String, MultiplayerClient> getAll_clients() {
		return all_clients;
	}
	public HashMap<String, MultiplayerClient> getOnline_clients() {
		return online_clients;
	}
	public HashMap<String, MultiplayerClient> getOffline_clients() {
		return offline_clients;
	}
	public HashMap<String, String> getOnline_user_info() {
		return online_user_info;
	}
	public HashMap<String, String> getOffline_user_info() {
		return offline_user_info;
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
		if(obj instanceof InfoPack){
			handleInfoPack(obj, client);
		}
		else if(obj instanceof Double){
			handleUserCommand(obj, client);
		}
		else{
			broadcastMessageToAll(obj);
		}
	}
	/**
	 * Sends incoming objects to all online clients.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void sendIncoming() throws ClassNotFoundException, IOException{
		InfoPack pack;

		for (MultiplayerClient client : online_clients.values()) {
			if(client.readPackage() instanceof InfoPack){
				pack = (InfoPack) client.readPackage();
				if(client.getUsername().contentEquals(pack.getReceiver())){
					client.sendPackage(pack);
				}
			}else{
				client.sendPackage(client.readPackage());
			}
		}
	}
	/**
	 * Sends a specific object to all online clients.
	 * @param obj
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void sendPackage(Object obj) throws ClassNotFoundException, IOException{
		for (MultiplayerClient client : online_clients.values()) {
				client.sendPackage(obj);
		}
	}
	/**
	 * Sends a welcome message to a specified client
	 * @param client
	 * @param obj
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void sendWelcomeMessage(MultiplayerClient client, Object obj) throws ClassNotFoundException, IOException{
		for(MultiplayerClient clients: online_clients.values()){
			if(!clients.getUsername().contentEquals(client.getUsername())){
				clients.sendPackage(obj);
			}
		}
	}
	/**
	 * Sends a list containing all users that are currently online
	 * to all the online clients.
	 * @param obj
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void sendListOfOnlineUsersToClient(Object obj) throws ClassNotFoundException, IOException {
		for (MultiplayerClient client : online_clients.values()) {
			client.sendPackage(obj);
		}
	}
	/**
	 * Adds unique client to a list containing all clients that
	 * have logged into the server.
	 * @param client
	 */
	public void addToDatabase(MultiplayerClient client){
		if(!all_clients.containsKey(client.getUsername())) {
			all_clients.put(client.getUsername(),client);
		}
	}
	/**
	 * Adds an offline client to a list containing all offline clients.
	 * @param client
	 */
	public void addOfflineUser(MultiplayerClient client){
		if(!offline_clients.containsKey(client.getUsername())) {
			offline_clients.put(client.getUsername(),client);
		}
	}
	/**
	 * returns a list containing the username of all the users
	 * that have logged into the server.
	 * @return
	 */
	public LinkedList<String> getAllUsers() {
		LinkedList<String> onlineUsers = new LinkedList<String>();
		for(String client: all_clients.keySet()){
			onlineUsers.add(client);
		}
		return onlineUsers;
	}
	/**
	 * returns a list containing the username of all users that
	 * have disconnected from the server.
	 * @return
	 */
	public LinkedList<String> getDisconnectedUsers(){
		LinkedList<String> offlineUsers = new LinkedList<String>();
		for(String client: offline_clients.keySet()){
			offlineUsers.add(client);
		}
		return offlineUsers;
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
		InfoPack pack = (InfoPack) obj;
		Boolean isImage = pack.getImageStatus();
		String sender = pack.getID();
		String getter = pack.getReceiver();
		String message = pack.getMessage();
		String imageName = pack.getImageName();
		String sms = sender + ": " + message;
		if (pack.getCommand() == null) {
			if(online_clients.containsKey(getter)){
				online_clients.get(getter).sendPackage(sms);
			}

		}
		else if (pack.getCommand() != null) {
			handleSpecialPackage(client, pack);
		}
	}
	/**
	 * Sends an infopack to specified online client from a specified sender.
	 * @param member
	 * @param pack
	 * @param sender
	 */
	public void sendToOnlineMember(String member, InfoPack pack, String sender){
		if(online_clients.containsKey(member)){
			online_clients.get(member).sendPackage(pack);
		}
	}
	/**
	 * Sends a private message to a specific client.
	 * details about the client and the sender are
	 * stored in the infopack.
	 * @param pack
	 */
	public void sendPrivateMessage(InfoPack pack){
		String sender = pack.getID();
		String receiver = pack.getReceiver();
		String message = pack.getMessage();
		String time = GUI.getTime() + ":  "+ sender + ": "  + message;

		if(online_clients.containsKey(receiver)){
			online_clients.get(receiver).sendPackage(new InfoPack(sender,receiver ,message,5.0));
		}
	}
	/**
	 * Method used to send infopacks containing images or
	 * additional objects which are not messages. details about the client and the sender are
	 * stored in the infopack.
	 * @param pack
	 */
	public void sendPrivateObject(InfoPack pack){
		String sender = pack.getID();
		String receiver = pack.getReceiver();
		String message = pack.getMessage();
		if(online_clients.containsKey(receiver)){
			online_clients.get(receiver).sendPackage(new InfoPack(sender,null,message,9.0,pack.getData()));
		}
	}

	/**
	 * Method which sends a private chat confirmation
	 * this method will send a confirmation to the receiver
	 * and the starter of the private chat
	 * @param pack
	 */
	public void privateChatConfirm(InfoPack pack){
		String sender = pack.getID();
		String receiver = pack.getReceiver();
		String message = pack.getMessage();

		if(online_clients.containsKey(receiver)){
			if(online_clients.get(receiver).isConnected()){
				online_clients.get(receiver).sendPackage(new InfoPack(sender,receiver ,message,4.0));
			}
		}
		if(online_clients.containsKey(sender)){
				online_clients.get(sender).sendPackage(new InfoPack("Server:", sender, "You have started a private chat with "+receiver, 5.0));

		}
	}
	/**
	 * Sends a private chat start confirmation to the starter of the chat.
	 * @param recipient
	 * @param pack
	 * @param sender
	 */
	public void sendToMember(MultiplayerClient recipient, InfoPack pack, String sender){
		recipient.sendPackage(pack);
		if(online_clients.containsKey(sender)){
			online_clients.get(sender).sendPackage(new InfoPack("Server:", sender,"You have started a private chat with: " + recipient.getUsername(), 5.0));
		}
	}
	/**
	 * Sends an infopack to all the online clients but the sender.
	 * @param pack
	 */
	public void sendDataPack(InfoPack pack) {
		String sender = pack.getID();
		for (MultiplayerClient client : online_clients.values()) {
			if (!client.getUsername().contentEquals(sender)) {
				client.sendPackage(new InfoPack(sender, 8.0, pack.getData()));
			}
			if (client.getUsername().contentEquals(sender)) {
				client.sendPackage("Server: You have sent an image!");
			}
		}
	}
	/**
	 * Method responsible for handling of packages containing special command
	 * the method will then determine an action base on the command given
	 * confined within the package.
	 * @param client
	 * @param pack
	 */
	public void handleSpecialPackage(MultiplayerClient client, InfoPack pack){
		if	(pack.getCommand() == 1.0){
		}
		else if(pack.getCommand() == 2.0){
			sendPrivateMessage(pack);
		}
		else if(pack.getCommand() == 3.0){
			privateChatConfirm(pack);
		}
		else if(pack.getCommand() == 5.0){
			sendDataPack(pack);
		}
		else if(pack.getCommand() == 6.0){
			sendPrivateObject(pack);
		}
	}
	/**
	 * This method is in charge of receiving login information.
	 * @param object
	 * @param client
	 */
	public synchronized void receiveNewUser(Object object, MultiplayerClient client) {
		InfoPack pack;
		if(object instanceof InfoPack){
			pack = (InfoPack)object;
			if(pack.getCommand() == 4.0){
				processUserInformation(pack, client);
			}
		}
	}
	/**
	 * This method is responsible for processing the login details
	 * of a new connecting client.
	 * @param pack
	 * @param client
	 */
	public void processUserInformation(InfoPack pack, MultiplayerClient client){
		client.setName(pack.getID());
		client.setUsername(pack.getID());
		client.setPassword(pack.getReceiver());
	}
	/**
	 * This method is used for sending updated list containing
	 * online users and offline users.
	 * @param client
	 */
	public void confirmOnlineUsers(String client){
		InfoPack pack =	new InfoPack("Server: ", client, getAllUsers(),"List of online users", 1.0);
		InfoPack pack2 = new InfoPack("", "", getDisconnectedUsers(),"",10.0);
		try {
			sendPackage(pack);
			sendPackage(pack2);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Broadcasts a messages to all connected users.
	 * @param obj
	 */
	public void broadcastMessageToAll(Object obj){
		String message = (String)obj;
		String time = GUI.getTime() + ":  " + message;
		if(GUI.getShowClientLog()==true){
			GUI.showServerEvent(message);
		}
		for (MultiplayerClient client : online_clients.values()) {
			client.sendPackage(obj);
		}
	}
	/**
	 * Handles a variety of commands sent by clients.
	 * this method is responsible for processing commands such
	 * as disconnect request.
	 * @param obj
	 * @param client
	 */
	public void handleUserCommand(Object obj, MultiplayerClient client){
		Double command = (Double) obj;
		Double closingCommand = 1.0;

		if (command == 1.0) {
			online_clients.remove(client.getUsername());
			addOfflineUser(client);
			rearrangeLists(client);
			client.sendPackage(closingCommand);
			client.setConnection(false);
			client.closeConnection();
			//
			confirmOnlineUsers(client.getUsername());
			//
			try {
				sendPackage(new InfoPack("Server: ", client.getUsername(), getDisconnectedUsers(),"",2.0));
				sendPackage(new InfoPack(client.getUsername(),"All users",getAllUsers(),client.getUsername(),1.0));
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
			client = null;
		}
		else if (command == 2.0) {
			try {
				sendPackage(new InfoPack(client.getUsername(),"All users",getAllUsers(),client.getUsername(),1.0));
				sendPackage(new InfoPack(client.getUsername(),"All users",getDisconnectedUsers(),client.getUsername(),10.0));
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		else if (command == 4.0) {

		}
	}
	/**
	 * Removes a clients information from an online list and moves it into a offline client
	 * information list.
	 * @param client
	 */
	public void rearrangeLists(MultiplayerClient client){
		online_user_info.remove(client.getPassword());
		offline_user_info.put(client.getPassword(), client.getUsername());
	}

	public void closeConnection(MultiplayerClient client){
		client.closeConnection();
	}
	public void removeClient(MultiplayerClient client) {
		this.online_clients.remove(client);
	}
	public void clearAll() {
		this.online_clients.clear();
		this.offline_clients.clear();
		this.all_clients.clear();
	}
	public MultiplayerClient findSpecificClient(String id) {
		return online_clients.get(id);
	}
	public void showOnConsole(String event){
		System.out.println("ClientManager: " + event);
	}
	public void disconnectAll() {
		for (MultiplayerClient client : online_clients.values()) {
			Double closingCommand = 1.0;
			client.sendPackage(closingCommand);
			client.setConnection(false);
			client.closeConnection();
			client = null;
		}
	}
	public void disconnectClient2(MultiplayerClient client) {
		Double closingCommand = 1.0;
		online_clients.remove(client);
		addOfflineUser(client);
		rearrangeLists(client);
		client.sendPackage(closingCommand);
		client.setConnection(false);
		client.closeConnection();
		//
		confirmOnlineUsers(client.getUsername());
		//
		try {
			sendPackage(new InfoPack("Server: ", client.getUsername(), getDisconnectedUsers(),"",2.0));
			sendPackage(new InfoPack(client.getUsername(),"All users",getAllUsers(),client.getUsername(),1.0));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		client = null;
	}
}