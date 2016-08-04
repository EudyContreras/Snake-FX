package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.EudyContreras.Snake.DataPackage.ServerResponse;
import com.EudyContreras.Snake.DataPackage.PlayerDetails;

/**
 * This class is used to handle clients and all actions from each client
 * this class organizes the logic used to send objects to specific clients
 * and it also handles the information that travels through the server.
 * @author Eudy Contreras, Mikael Malmgren, Johannes Berggren
 *
 */
public class ClientManager extends MultiplayerServer{

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
	public ClientManager(ServerGUI GUI) {
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
	 * Method which sends a private chat confirmation
	 * this method will send a confirmation to the receiver
	 * and the starter of the private chat
	 * @param pack
	 */
	public void privateChatConfirm(ServerResponse pack){
		String sender = pack.getID();
		String receiver = pack.getReceiver();
		String message = pack.getMessage();

		if(online_clients.containsKey(receiver)){
			if(online_clients.get(receiver).isConnected()){
				online_clients.get(receiver).sendPackage(new ServerResponse(sender,receiver ,message,4.0));
			}
		}
		if(online_clients.containsKey(sender)){
				online_clients.get(sender).sendPackage(new ServerResponse("Server:", sender, "You have started a private chat with "+receiver, 5.0));

		}
	}

	/**
	 * This method is in charge of receiving login information.
	 * @param object
	 * @param client
	 */
	public synchronized void receiveNewUser(Object object, MultiplayerClient client) {
		PlayerDetails pack;
		if(object instanceof ServerResponse){
			pack = (PlayerDetails)object;
			processUserInformation(pack, client);
		}
	}
	/**
	 * This method is responsible for processing the login details
	 * of a new connecting client.
	 * @param pack
	 * @param client
	 */
	public void processUserInformation(PlayerDetails pack, MultiplayerClient client){
		client.setName(pack.getName());
		client.setUsername(pack.getUserName());
		client.setPassword(pack.getPassWord());
	}
	/**
	 * This method is used for sending updated list containing
	 * online users and offline users.
	 * @param client
	 */
	public void confirmOnlineUsers(String client){
		ServerResponse pack =	new ServerResponse("Server: ", client, getAllUsers(),"List of online users", 1.0);
		ServerResponse pack2 = new ServerResponse("", "", getDisconnectedUsers(),"",10.0);
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
	public void sendPackage(Object obj) throws ClassNotFoundException, IOException{
		for (MultiplayerClient client : online_clients.values()) {
				client.sendPackage(obj);
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
			sendPackage(new ServerResponse("Server: ", client.getUsername(), getDisconnectedUsers(),"",2.0));
			sendPackage(new ServerResponse(client.getUsername(),"All users",getAllUsers(),client.getUsername(),1.0));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		client = null;
	}
}