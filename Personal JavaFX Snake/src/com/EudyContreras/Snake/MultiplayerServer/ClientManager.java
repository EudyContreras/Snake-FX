package com.EudyContreras.Snake.MultiplayerServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.EudyContreras.Snake.Commands.ServerCommand;
import com.EudyContreras.Snake.DataPackage.PlayerDetails;
import com.EudyContreras.Snake.DataPackage.ServerEvent;
import com.EudyContreras.Snake.DataPackage.ServerResponse;

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
	private HashMap<String, String> online_user_info; // Key: passWord, Value: userName
	private HashMap<String, String> offline_user_info; // Key: passWord, Value: userName

	private byte[] dafaultPicture;

	private ClientAuthentification authentification;
	/**
	 * Constructor which takes the GUI as an argument.
	 * @param GUI the servers graphical user interface.
	 */
	public ClientManager() {
		super();
		all_clients = new HashMap<>();
		online_clients = new HashMap<>();
		offline_clients = new HashMap<>();
		online_user_info = new HashMap<>();
		offline_user_info = new HashMap<>();
		authentification = new ClientAuthentification(this);
	}

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
	 * Receives a new connecting client and sends it
	 * to be revised for uniqueness.
	 * @param client
	 */
	public synchronized void addIncominglient(MultiplayerClient client) {
		authentification.performInstanceCheck(client);
	}
	/**
	 * This method is in charge of receiving login information.
	 * @param object
	 * @param client
	 */
	public synchronized void receiveNewUser(Object object, MultiplayerClient client) {
		PlayerDetails pack = (PlayerDetails) object;
		processUserInformation(pack, client);

	}
	/**
	 * This method is responsible for processing the login details
	 * of a new connecting client.
	 * @param pack
	 * @param client
	 */
	public void processUserInformation(PlayerDetails pack, MultiplayerClient client){
		client.setPlayerName(pack.getName());
		client.setPassword(pack.getPassWord());
		client.setAge(pack.getAge());
		client.setLevel(pack.getLevel());

		if (pack.getUserName() != null) {
			client.setUserName(pack.getUserName());
		}
		else{
			createUserName(client);
		}

		if(client.getProfilePic()!=null){
			client.setProfilePic(pack.getProfilePicture());
		}
		else{
			client.setProfilePic(dafaultPicture);
		}
	}
	private void createUserName(MultiplayerClient client){
		if(!clientExist("Player" + MultiplayerServer.UNIQUE_ID)){
			client.setUserName("Player" + MultiplayerServer.UNIQUE_ID);
		}
		else{
			client.setUserName("Player"+client.getSocketAddress().toString()+MultiplayerServer.UNIQUE_ID);
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
		for (Map.Entry<String, MultiplayerClient> entry : online_clients.entrySet()) {
			MultiplayerClient clients = entry.getValue();
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
		for (Map.Entry<String, MultiplayerClient> entry : online_clients.entrySet()) {
			entry.getValue().sendPackage(obj);

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
			online_clients.remove(client.getUsername(), client);
		}
	}

	public void removeFromServer(MultiplayerClient client){
		if(online_clients.containsKey(client.getUsername())){
			online_clients.remove(client.getUsername(), client);
		}
		if(all_clients.containsKey(client.getUsername())){
			all_clients.remove(client.getUsername(), client);
		}
		if(online_user_info.containsValue(client.getUsername())) {
			online_user_info.remove(client.getUsername(), client);
		}
	}
	/**
	 * returns a list containing the username of all the users
	 * that have logged into the server.
	 * @return
	 */
	public LinkedList<String> getAllUsers() {
		LinkedList<String> onlineUsers = new LinkedList<String>(all_clients.keySet());
		return onlineUsers;
	}
	/**
	 * returns a list containing the username of all users that
	 * have disconnected from the server.
	 * @return
	 */
	public LinkedList<String> getOfflineClients(){
		LinkedList<String> offlineUsers = new LinkedList<String>(offline_clients.keySet());
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

		online_clients.get(receiver).sendPackage(new ServerResponse(sender, receiver, message, 4.0));
		online_clients.get(sender).sendPackage(
				new ServerResponse("Server:", sender, "You have started a private chat with " + receiver, 5.0));

	}


	/**
	 * This method is used for sending updated list containing
	 * online users and offline users.
	 * @param client
	 */
	public void updateOnlineClients(String client){
		ServerResponse pack =	new ServerResponse("Server: ", client, getAllUsers(),"List of online users", 1.0);
		ServerResponse pack2 = new ServerResponse("", "", getOfflineClients(),"",10.0);
		try {
			sendBroadcastPackage(pack);
			sendBroadcastPackage(pack2);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Broadcasts a messages to all connected users.
	 * @param obj
	 */
	public void sendBroadcastPackage(Object obj) throws ClassNotFoundException, IOException{
		for (Map.Entry<String, MultiplayerClient> entry : online_clients.entrySet()) {
			entry.getValue().sendPackage(obj);
		}
	}
	/**
	 * Removes a clients information from an online list and moves it into a offline client
	 * information list.
	 * @param client
	 */
	public void rearrangeLists(MultiplayerClient client){
		online_user_info.remove(client.getPassword(), client.getUsername());
		offline_user_info.put(client.getPassword(), client.getUsername());
	}

	public void closeConnection(MultiplayerClient client){
		client.closeConnection();
	}
	public void removeClient(MultiplayerClient client) {
		this.online_clients.remove(client.getUsername(),client);
	}
	public void clearAll() {
		this.online_clients.clear();
		this.offline_clients.clear();
		this.all_clients.clear();
	}
	public MultiplayerClient findSpecificClient(String id) {
		return online_clients.get(id);
	}
	public boolean clientExist(String userName){
		if(online_clients.containsKey(userName)){ return true;}
		else if(offline_clients.containsKey(userName)){return true;}
		else{return false;}
	}

	public void disconnectAll() {
		for (Map.Entry<String, MultiplayerClient> entry : online_clients.entrySet()) {
			MultiplayerClient client = entry.getValue();
			client.sendPackage(new ServerEvent(ServerCommand.DISCONNECT));
			client.setConnection(false);
			client.closeConnection();
			client = null;
		}
	}

	public void disconnectClient(MultiplayerClient client) {
		if (!client.isGuest()) {
			addOfflineUser(client);
			rearrangeLists(client);
		}
		else {
			removeFromServer(client);
		}
		client.sendPackage(new ServerEvent(ServerCommand.DISCONNECT));
		client.setConnection(false);
		client.closeConnection();

		updateOnlineClients(client.getUsername());

		try {
			sendBroadcastPackage(new ServerResponse("Server: ", client.getUsername(), getOfflineClients(),"",2.0));
			sendBroadcastPackage(new ServerResponse(client.getUsername(),"All users",getAllUsers(),client.getUsername(),1.0));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		client = null;
	}

	public void showOnConsole(String event){
		System.out.println("ClientManager: " + event);
	}
}